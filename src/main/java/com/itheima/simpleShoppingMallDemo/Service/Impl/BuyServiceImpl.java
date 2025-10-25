package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.*;
import com.itheima.simpleShoppingMallDemo.Model.*;
import com.itheima.simpleShoppingMallDemo.ModelDto.CartBuyDto;
import com.itheima.simpleShoppingMallDemo.ModelDto.CartItemProductDto;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserAddressAndCartDto;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserAddressAndProductDto;
import com.itheima.simpleShoppingMallDemo.Service.AddressService;
import com.itheima.simpleShoppingMallDemo.Service.BuyService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("BuyService")
public class BuyServiceImpl extends ServiceImpl<OrderMapper, Order> implements BuyService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    BalanceUsageRecordMapper balanceUsageRecordMapper;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    CartItemMapper cartItemMapper;
    @Autowired
    ShipmentMapper shipmentMapper;
    @Autowired
    AddressService addressService;

    // 查询用户和商品信息
    @Override
    public Result<UserAddressAndProductDto> selUserProductByUidAndPid(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return Result.fail("值为空");
        }
        Product product = productMapper.selectById(productId);
        List<Address> addressList = addressMapper.selectList(
                new LambdaQueryWrapper<Address>()
                        .eq(Address::getUserId, userId)
                        .last("LIMIT 10")
        );

        if (addressList!=null && product != null) {
            UserAddressAndProductDto userAddressAndProductDto = new UserAddressAndProductDto();

            userAddressAndProductDto.setAddressList(addressList);//放入地址表

            userAddressAndProductDto.setName(product.getName());
            userAddressAndProductDto.setPrice(product.getPrice());
            userAddressAndProductDto.setProductUrl(product.getProductUrl());
            return Result.success(userAddressAndProductDto);
        } else {
            return Result.fail("查询失败");
        }
    }

    // 创建订单并处理库存更新（支持单品和购物车）
    @Override
    @Transactional
    public Result<Long> createOrderByUsernameAndQuantityAndPid(Long userId, CartBuyDto cartBuyDto) {
        // 1. 获取订单商品列表（单品或购物车）
        List<OrderItemDto> orderItemDtos = getOrderItems(userId, cartBuyDto);

        if (orderItemDtos == null || orderItemDtos.isEmpty()) {
            return Result.fail("订单商品为空");
        }

        // 2. 检查所有商品库存
        for (OrderItemDto itemDto : orderItemDtos) {
            Product product = productMapper.selectById(itemDto.getProductId());
            if (product == null) {
                return Result.fail("商品不存在: " + itemDto.getProductId());
            }

            Integer availableStock = product.getStock();
            if (availableStock == null || availableStock < itemDto.getQuantity()) {
                return Result.fail("商品库存不足: " + product.getName());
            }

            // 保存商品信息到DTO
            itemDto.setPrice(product.getPrice());
            itemDto.setProduct(product);
        }

        // 3. 计算订单总价
        BigDecimal totalPrice = orderItemDtos.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setStatus(0L); // 待支付
        order.setAddressId(cartBuyDto.getAddressId()); // 设置收货地址
        save(order);

        if (order.getOrderId() == null) {
            throw new RuntimeException("创建订单失败，orderId 为空");
        }

        // 5. 创建订单明细和发货记录
        for (OrderItemDto itemDto : orderItemDtos) {
            // 创建订单明细
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setProductId(itemDto.getProductId());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(itemDto.getPrice());

            int row = orderItemMapper.insert(orderItem);
            if (row <= 0) {
                throw new RuntimeException("订单明细插入失败");
            }

            // 创建发货记录（paymentId 为 null）
            Shipment shipment = new Shipment();
            shipment.setOrderId(order.getOrderId());
            shipment.setOrderItemId(orderItem.getOrderItemId());
            shipment.setProductId(itemDto.getProductId());
            shipment.setUserId(userId);
            shipment.setQuantity(itemDto.getQuantity());
            shipment.setAddressId(cartBuyDto.getAddressId());
            shipment.setShipmentStatus(0L); // 待发货
            shipment.setPaymentId(null); // 暂时为空，支付时更新

            int shipmentRow = shipmentMapper.insert(shipment);
            if (shipmentRow <= 0) {
                throw new RuntimeException("发货记录创建失败");
            }

            // 更新库存（使用乐观锁防止超卖）
            Integer newStock = itemDto.getProduct().getStock() - itemDto.getQuantity();
            LambdaUpdateWrapper<Product> stockUpdateWrapper = Wrappers.lambdaUpdate();
            stockUpdateWrapper
                    .set(Product::getStock, newStock)
                    .eq(Product::getProductId, itemDto.getProductId())
                    .eq(Product::getStock, itemDto.getProduct().getStock()); // 乐观锁

            int stockUpdateResult = productMapper.update(stockUpdateWrapper);
            if (stockUpdateResult <= 0) {
                throw new RuntimeException("库存更新失败，可能库存不足: " + itemDto.getProduct().getName());
            }
        }

        // 6. 如果是购物车结算，清空购物车
        if (cartBuyDto.getCartItemIds() != null && !cartBuyDto.getCartItemIds().isEmpty()) {
            LambdaUpdateWrapper<CartItem> deleteWrapper = Wrappers.lambdaUpdate();
            deleteWrapper.in(CartItem::getCartItemId, cartBuyDto.getCartItemIds());
            cartItemMapper.delete(deleteWrapper);
        }

        return Result.success(order.getOrderId());
    }

    // 创建支付并更新余额
    @Override
    @Transactional
    public Result<Boolean> createPaymentByUsernameAndQuantityAndPid(Long userId, CartBuyDto cartBuyDto) {
        // 1. 先调用创建订单接口
        Result<Long> orderResult = createOrderByUsernameAndQuantityAndPid(userId, cartBuyDto);

        if (!orderResult.isSuccess()) {
            return Result.fail(orderResult.getMessage());
        }

        Long orderId = orderResult.getData();

        // 2. 查询订单信息
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单查询失败");
        }

        // 3. 检查用户余额
        User user = userMapper.selectById(userId);
        if (user.getBalance() == null || order.getTotalPrice() == null
                || user.getBalance().compareTo(BigDecimal.ZERO) <= 0
                || order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0
                || user.getBalance().compareTo(order.getTotalPrice()) < 0) {
            throw new RuntimeException("余额不足，或值为空/无效");
        }

        // 4. 创建支付记录
        Payment payment = new Payment();
        BigDecimal endBalance = user.getBalance().subtract(order.getTotalPrice());

        payment.setUserId(userId);
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalPrice());
        payment.setStatus(1L); // 支付成功

        int paymentResult = paymentMapper.insert(payment);
        if (paymentResult <= 0) {
            throw new RuntimeException("支付明细插入失败");
        }

        // 5. 更新发货表的 paymentId
        LambdaUpdateWrapper<Shipment> shipmentUpdateWrapper = Wrappers.lambdaUpdate();
        shipmentUpdateWrapper
                .set(Shipment::getPaymentId, payment.getPaymentId())
                .eq(Shipment::getOrderId, orderId);

        int shipmentUpdateResult = shipmentMapper.update(shipmentUpdateWrapper);
        if (shipmentUpdateResult <= 0) {
            throw new RuntimeException("发货表 paymentId 更新失败");
        }

        // 6. 创建余额使用记录
        BalanceUsageRecord balanceUsageRecord = new BalanceUsageRecord();
        balanceUsageRecord.setUserId(userId);
        balanceUsageRecord.setPaymentId(payment.getPaymentId());
        balanceUsageRecord.setBalanceBefore(user.getBalance());
        balanceUsageRecord.setBalanceUsed(order.getTotalPrice());
        balanceUsageRecord.setBalanceAfter(endBalance);
        balanceUsageRecord.setTransactionType("消费");

        int recordResult = balanceUsageRecordMapper.insert(balanceUsageRecord);
        if (recordResult <= 0) {
            throw new RuntimeException("余额使用记录插入失败");
        }

        // 7. 更新用户余额
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper
                .set(User::getBalance, endBalance)
                .eq(User::getUserId, userId);

        int balanceUpdateResult = userMapper.update(updateWrapper);
        if (balanceUpdateResult <= 0) {
            throw new RuntimeException("用户余额更新失败");
        }

        // 8. 更新订单状态为已支付
        LambdaUpdateWrapper<Order> orderUpdateWrapper = Wrappers.lambdaUpdate();
        orderUpdateWrapper
                .set(Order::getStatus, 1L)
                .eq(Order::getOrderId, orderId);

        int orderUpdateResult = orderMapper.update(orderUpdateWrapper);
        if (orderUpdateResult <= 0) {
            throw new RuntimeException("订单状态更新失败");
        }

        return Result.success(true);
    }

    // 辅助方法：获取订单商品列表（支持单品和购物车）
    private List<OrderItemDto> getOrderItems(Long userId, CartBuyDto cartBuyDto) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();

        // 单品购买
        if (cartBuyDto.getProductId() != null && cartBuyDto.getQuantity() != null) {
            OrderItemDto dto = new OrderItemDto();
            dto.setProductId(cartBuyDto.getProductId());
            dto.setQuantity(cartBuyDto.getQuantity());
            orderItemDtos.add(dto);
        }
        // 购物车购买
        else if (cartBuyDto.getCartItemIds() != null && !cartBuyDto.getCartItemIds().isEmpty()) {
            LambdaQueryWrapper<CartItem> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper
                    .eq(CartItem::getUserId, userId)
                    .in(CartItem::getCartItemId, cartBuyDto.getCartItemIds());

            List<CartItem> cartItems = cartItemMapper.selectList(queryWrapper);

            for (CartItem cartItem : cartItems) {
                OrderItemDto dto = new OrderItemDto();
                dto.setProductId(cartItem.getProductId());
                dto.setQuantity(cartItem.getQuantity());
                orderItemDtos.add(dto);
            }
        }

        return orderItemDtos;
    }

    // 内部DTO类
    @Data
    private static class OrderItemDto {
        private Long productId;
        private Integer quantity;
        private BigDecimal price;
        private Product product;
    }


    @Override
    public Result<UserAddressAndCartDto> getCartListWithAddress(Long userId, String cartItemIds) {
        try {
            // 1. 获取用户地址列表
            List<Address> addressList = addressService.listByUserId(userId);

            // 2. 解析购物车ID列表
            String[] idArray = cartItemIds.split(",");
            List<CartItemProductDto> cartItemList = new ArrayList<>();

            for (String idStr : idArray) {
                Long cartItemId = Long.parseLong(idStr.trim());

                // 获取购物车项信息
                CartItem cart = cartItemMapper.selByUserIdAndCartItemId(userId, cartItemId);
                if (cart == null) {
                    continue;
                }

                // 获取商品信息
                Product product = productMapper.selectById(cart.getProductId());
                if (product == null) {
                    continue;
                }

                // 组装 DTO
                CartItemProductDto dto = new CartItemProductDto();
                dto.setCartItemId(cart.getCartItemId());
                dto.setProductId(product.getProductId());
                dto.setName(product.getName());
                dto.setDescription(product.getDescription());
                dto.setPrice(product.getPrice());
                dto.setQuantity(cart.getQuantity());
                dto.setProductUrl(product.getProductUrl());

                cartItemList.add(dto);
            }

            // 3. 组装返回数据
            UserAddressAndCartDto result = new UserAddressAndCartDto();
            result.setAddressList(addressList);
            result.setCartItems(cartItemList);

            return Result.success(result);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("获取购物车结算数据失败");
        }
    }
}
