package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.*;
import com.itheima.simpleShoppingMallDemo.Model.*;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserProductDto;
import com.itheima.simpleShoppingMallDemo.Service.BuyService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

    // 查询用户和商品信息
    @Override
    public Result<UserProductDto> selUserProductByUidAndPid(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return Result.fail("值为空");
        }
        User user = userMapper.selectById(userId);
        Product product = productMapper.selectById(productId);

        if (user != null && product != null) {
            UserProductDto userProductDto = new UserProductDto();

            userProductDto.setUsername(user.getUsername());
            userProductDto.setPhone(user.getPhone());
            userProductDto.setAddress(user.getAddress());

            userProductDto.setName(product.getName());
            userProductDto.setPrice(product.getPrice());
            userProductDto.setProductUrl(product.getProductUrl());
            return Result.success(userProductDto);
        } else {
            return Result.fail("查询失败");
        }
    }

    // 创建订单并处理库存更新
    @Override
    @Transactional
    public Result<Boolean> createOrderByUsernameAndQuantityAndPid(Long userId, OrderItem orderItem) {
        Product product = productMapper.selectById(orderItem.getProductId());

        if (userId == null || orderItem.getQuantity() == null || product.getPrice() == null) {
            return Result.fail("quantity或price其中值为空");
        }

        // 在创建订单之前先检查库存
        Integer availableStock = product.getStock();
        if (availableStock == null || availableStock < orderItem.getQuantity()) {
            return Result.fail("库存不足");
        }

        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(product.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        order.setStatus(0L); // 假设状态0表示待支付
        save(order);

        if (order.getOrderId() == null) {
            throw new RuntimeException("创建订单失败，orderId 为空");
        }

        // 创建订单明细
        orderItem.setOrderId(order.getOrderId());
        orderItem.setPrice(product.getPrice());
        int row = orderItemMapper.insert(orderItem);

        if (row <= 0) {
            throw new RuntimeException("订单明细插入失败");
        }

        // 更新库存
        Integer newStock = availableStock - orderItem.getQuantity();
        LambdaUpdateWrapper<Product> stockUpdateWrapper = Wrappers.lambdaUpdate();
        stockUpdateWrapper
                .set(Product::getStock, newStock)
                .eq(Product::getProductId, orderItem.getProductId());
        int stockUpdateResult = productMapper.update(stockUpdateWrapper);

        if (stockUpdateResult <= 0) {
            throw new RuntimeException("库存更新失败");
        }

        return Result.success(true);
    }

    // 创建支付并更新余额
    @Override
    @Transactional
    public Result<Boolean> createPaymentByUsernameAndQuantityAndPid(Long userId, OrderItem orderItem) {
        // 先调用createOrderByUsernameAndQuantityAndPid创建订单并更新库存
        Result<Boolean> orderResult = createOrderByUsernameAndQuantityAndPid(userId, orderItem);

        if (!orderResult.isSuccess()) {
            return orderResult;  // 如果订单创建失败，则直接返回失败
        }

        // 创建支付相关逻辑
        Product product = productMapper.selectById(orderItem.getProductId());
        Order order = orderMapper.selectById(orderItem.getOrderId());

        if (order == null) {
            throw new RuntimeException("订单查询失败");
        }

        // 检查用户余额
        User user = userMapper.selectById(userId);
        if (user.getBalance() == null || order.getTotalPrice() == null
                || user.getBalance().compareTo(BigDecimal.ZERO) <= 0
                || order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0
                || user.getBalance().compareTo(order.getTotalPrice()) < 0) {

            throw new RuntimeException("余额不足，或值为空/无效");
        }

        // 处理支付信息
        Payment payment = new Payment();
        BigDecimal endBalance = user.getBalance().subtract(order.getTotalPrice());

        payment.setUserId(userId);
        payment.setOrderId(order.getOrderId());
        payment.setAmount(order.getTotalPrice());
        payment.setStatus(1L); // 支付成功

        int paymentResult = paymentMapper.insert(payment);
        if (paymentResult <= 0) {
            throw new RuntimeException("支付明细插入失败");
        }

        // 更新用户余额
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper
                .set(User::getBalance, endBalance)
                .eq(User::getUserId, userId);
        int balanceUpdateResult = userMapper.update(updateWrapper);

        if (balanceUpdateResult <= 0) {
            throw new RuntimeException("用户余额更新失败");
        }

        // 更新订单状态为已支付（1）
        LambdaUpdateWrapper<Order> orderUpdateWrapper = Wrappers.lambdaUpdate();
        orderUpdateWrapper
                .set(Order::getStatus, 1L)  // 设置为已支付
                .eq(Order::getOrderId, order.getOrderId());
        int orderUpdateResult = orderMapper.update(orderUpdateWrapper);

        if (orderUpdateResult <= 0) {
            throw new RuntimeException("订单状态更新失败");
        }

        return Result.success(true);
    }
}
