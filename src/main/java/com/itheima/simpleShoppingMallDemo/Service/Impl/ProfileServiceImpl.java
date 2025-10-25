package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.*;
import com.itheima.simpleShoppingMallDemo.Model.*;
import com.itheima.simpleShoppingMallDemo.ModelDto.*;
import com.itheima.simpleShoppingMallDemo.Service.ProfileService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("profileService")
public class ProfileServiceImpl extends ServiceImpl<UserMapper, User> implements ProfileService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private BalanceUsageRecordMapper balanceUsageRecordMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserFavoriteProductMapper userFavoriteProductMapper;

    @Autowired
    private CommentMapper commentMapper;

    // ==================== 地址管理 ====================

    /**
     * 查询用户所有收货地址
     */
    @Override
    public Result<List<Address>> getUserAddresses(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .eq(Address::getHidden, 0)
                .orderByDesc(Address::getCreatedAt);

        List<Address> addresses = addressMapper.selectList(wrapper);
        return Result.success(addresses);
    }

    /**
     * 添加收货地址
     */
    @Override
    public Result<Integer> addAddress(Address address) {
        address.setHidden(0);
        int result = addressMapper.insert(address);
        return result > 0 ? Result.success(result) : Result.fail("添加地址失败");
    }

    /**
     * 修改收货地址
     */
    @Override
    public Result<Integer> updateAddress(Address address) {
        int result = addressMapper.updateById(address);
        return result > 0 ? Result.success(result) : Result.fail("修改地址失败");
    }

    /**
     * 删除收货地址（逻辑删除）
     */
    @Override
    public Result<Integer> deleteAddress(Long addressId, Long userId) {
        Address address = addressMapper.selectById(addressId);
        if (address == null) {
            return Result.fail("地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            return Result.fail("无权删除此地址");
        }

        int result = addressMapper.deleteById(addressId);
        return result > 0 ? Result.success(result) : Result.fail("删除地址失败");
    }

    // ==================== 余额记录管理 ====================

    /**
     * 查询用户余额使用记录（关联支付和订单信息）
     */
    @Override
    public Result<List<BalanceUsageRecordDto>> getBalanceRecords(Long userId) {
        LambdaQueryWrapper<BalanceUsageRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BalanceUsageRecord::getUserId, userId)
                .eq(BalanceUsageRecord::getHidden, 0)
                .orderByDesc(BalanceUsageRecord::getCreatedAt);

        List<BalanceUsageRecord> records = balanceUsageRecordMapper.selectList(wrapper);
        List<BalanceUsageRecordDto> dtoList = new ArrayList<>();

        for (BalanceUsageRecord record : records) {
            BalanceUsageRecordDto dto = new BalanceUsageRecordDto();
            dto.setUsageId(record.getUsageId());
            dto.setPaymentId(record.getPaymentId());
            dto.setUserId(record.getUserId());
            dto.setBalanceBefore(record.getBalanceBefore());
            dto.setBalanceAfter(record.getBalanceAfter());
            dto.setBalanceUsed(record.getBalanceUsed());
            dto.setTransactionType(record.getTransactionType());
            dto.setCreatedAt(record.getCreatedAt());

            // 关联支付信息
            Payment payment = paymentMapper.selectById(record.getPaymentId());
            if (payment != null) {
                dto.setPaymentAmount(payment.getAmount());
                dto.setPaymentStatus(payment.getStatus());

                // 关联订单信息
                Order order = orderMapper.selectById(payment.getOrderId());
                if (order != null) {
                    dto.setOrderId(order.getOrderId());
                    dto.setOrderTotalPrice(order.getTotalPrice());
                    dto.setOrderStatus(order.getStatus());

                    // 关联商品信息
                    LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
                    itemWrapper.eq(OrderItem::getOrderId, order.getOrderId())
                            .eq(OrderItem::getHidden, 0);
                    List<OrderItem> orderItems = orderItemMapper.selectList(itemWrapper);

                    List<ProductDto> products = new ArrayList<>();
                    for (OrderItem item : orderItems) {
                        Product product = productMapper.selectById(item.getProductId());
                        if (product != null) {
                            ProductDto productDto = new ProductDto();
                            productDto.setProductId(product.getProductId());
                            productDto.setName(product.getName());
                            productDto.setPrice(item.getPrice());
                            productDto.setQuantity(item.getQuantity());
                            productDto.setProductUrl(product.getProductUrl());
                            products.add(productDto);
                        }
                    }
                    dto.setProducts(products);
                }
            }

            dtoList.add(dto);
        }

        return Result.success(dtoList);
    }

    /**
     * 删除余额记录（逻辑删除）
     */
    @Override
    public Result<Integer> deleteBalanceRecord(Long usageId, Long userId) {
        BalanceUsageRecord record = balanceUsageRecordMapper.selectById(usageId);
        if (record == null) {
            return Result.fail("记录不存在");
        }
        if (!record.getUserId().equals(userId)) {
            return Result.fail("无权删除此记录");
        }

        int result = balanceUsageRecordMapper.deleteById(usageId);
        return result > 0 ? Result.success(result) : Result.fail("删除记录失败");
    }

    // ==================== 商品收藏管理 ====================

    /**
     * 查询用户收藏的商品
     */
    @Override
    public Result<List<FavoriteProductDto>> getFavoriteProducts(Long userId) {
        LambdaQueryWrapper<UserFavoriteProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoriteProduct::getUserId, userId)
                .eq(UserFavoriteProduct::getHidden, 0)
                .orderByDesc(UserFavoriteProduct::getCreatedAt);

        List<UserFavoriteProduct> favorites = userFavoriteProductMapper.selectList(wrapper);
        List<FavoriteProductDto> dtoList = new ArrayList<>();

        for (UserFavoriteProduct favorite : favorites) {
            Product product = productMapper.selectById(favorite.getProductId());
            if (product != null && product.getHidden() == 0) {
                FavoriteProductDto dto = new FavoriteProductDto();
                dto.setFavoriteId(favorite.getFavoriteId());
                dto.setProductId(product.getProductId());
                dto.setProductName(product.getName());
                dto.setProductDescription(product.getDescription());
                dto.setProductPrice(product.getPrice());
                dto.setProductStock(product.getStock());
                dto.setProductUrl(product.getProductUrl());
                dto.setCreatedAt(favorite.getCreatedAt());
                dtoList.add(dto);
            }
        }

        return Result.success(dtoList);
    }

    /**
     * 删除商品收藏
     */
    @Override
    public Result<Integer> deleteFavoriteProduct(Long favoriteId, Long userId) {
        UserFavoriteProduct favorite = userFavoriteProductMapper.selectById(favoriteId);
        if (favorite == null) {
            return Result.fail("收藏不存在");
        }
        if (!favorite.getUserId().equals(userId)) {
            return Result.fail("无权删除此收藏");
        }

        int result = userFavoriteProductMapper.deleteById(favoriteId);
        return result > 0 ? Result.success(result) : Result.fail("取消收藏失败");
    }

    // ==================== 评论管理 ====================

    /**
     * 查询用户的所有评论
     */
    @Override
    public Result<List<CommentDto>> getUserComments(Long userId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getUserId, userId)
                .eq(Comment::getHidden, 0)
                .orderByDesc(Comment::getCreatedAt);

        List<Comment> comments = commentMapper.selectList(wrapper);
        List<CommentDto> dtoList = new ArrayList<>();

        for (Comment comment : comments) {
            Product product = productMapper.selectById(comment.getProductId());
            if (product != null) {
                CommentDto dto = new CommentDto();
                dto.setCommentId(comment.getCommentId());
                dto.setContent(comment.getContent());
                dto.setCommentImageUrl(comment.getCommentImageUrl());
                dto.setCreatedAt(comment.getCreatedAt());
                dto.setProductId(product.getProductId());
                dto.setProductName(product.getName());
                dto.setProductUrl(product.getProductUrl());
                dtoList.add(dto);
            }
        }

        return Result.success(dtoList);
    }

    /**
     * 删除评论（逻辑删除）
     */
    @Override
    public Result<Integer> deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            return Result.fail("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            return Result.fail("无权删除此评论");
        }

        int result = commentMapper.deleteById(commentId);
        return result > 0 ? Result.success(result) : Result.fail("删除评论失败");
    }

    // ==================== 支付记录管理 ====================

    /**
     * 查询用户的支付记录（关联订单和商品信息）
     */
    @Override
    public Result<List<PaymentRecordDto>> getPaymentRecords(Long userId) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getUserId, userId)
                .eq(Payment::getHidden, 0)
                .orderByDesc(Payment::getCreatedAt);

        List<Payment> payments = paymentMapper.selectList(wrapper);
        List<PaymentRecordDto> dtoList = new ArrayList<>();

        for (Payment payment : payments) {
            PaymentRecordDto dto = new PaymentRecordDto();
            dto.setPaymentId(payment.getPaymentId());
            dto.setAmount(payment.getAmount());
            dto.setStatus(payment.getStatus());
            dto.setCreatedAt(payment.getCreatedAt());

            // 关联订单信息
            Order order = orderMapper.selectById(payment.getOrderId());
            if (order != null) {
                dto.setOrderId(order.getOrderId());
                dto.setOrderTotalPrice(order.getTotalPrice());
                dto.setOrderStatus(order.getStatus());
                dto.setOrderCreatedAt(order.getCreatedAt());

                // 关联订单项和商品信息
                LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
                itemWrapper.eq(OrderItem::getOrderId, order.getOrderId())
                        .eq(OrderItem::getHidden, 0);
                List<OrderItem> orderItems = orderItemMapper.selectList(itemWrapper);

                List<OrderItemDto> orderItemDtos = new ArrayList<>();
                for (OrderItem item : orderItems) {
                    Product product = productMapper.selectById(item.getProductId());
                    if (product != null) {
                        OrderItemDto itemDto = new OrderItemDto();
                        itemDto.setOrderItemId(item.getOrderItemId());
                        itemDto.setProductId(product.getProductId());
                        itemDto.setProductName(product.getName());
                        itemDto.setProductUrl(product.getProductUrl());
                        itemDto.setQuantity(item.getQuantity());
                        itemDto.setPrice(item.getPrice());
                        orderItemDtos.add(itemDto);
                    }
                }
                dto.setOrderItems(orderItemDtos);
            }

            dtoList.add(dto);
        }

        return Result.success(dtoList);
    }

    /**
     * 删除支付记录（逻辑删除）
     */
    @Override
    public Result<Integer> deletePaymentRecord(Long paymentId, Long userId) {
        Payment payment = paymentMapper.selectById(paymentId);
        if (payment == null) {
            return Result.fail("支付记录不存在");
        }
        if (!payment.getUserId().equals(userId)) {
            return Result.fail("无权删除此支付记录");
        }

        int result = paymentMapper.deleteById(paymentId);
        return result > 0 ? Result.success(result) : Result.fail("删除支付记录失败");
    }

    /**
     * 查询和返回单个用户的信息
     */
    @Override
    public Result<UserDto> selUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username).eq(User::getHidden, 0);
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            return Result.fail("用户不存在");
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setAddress(user.getAddress());
        userDto.setBalance(user.getBalance());
        userDto.setNickname(user.getNickname());

        return Result.success(userDto);
    }

    /**
     * 更新用户的信息
     */
    @Override
    public Result<Integer> updateByUser(UserDto userDto) {
        User user = userMapper.selectById(userDto.getUserId());
        if (user == null) {
            return Result.fail("用户不存在");
        }

        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());
        if (userDto.getAddress() != null) user.setAddress(userDto.getAddress());
        if (userDto.getNickname() != null) user.setNickname(userDto.getNickname());

        int result = userMapper.updateById(user);
        return Result.success(result);
    }


    /**
     * 模拟用户充值功能
     */
    @Override
    @Transactional
    public Result<Boolean> rechargeByuser(BigDecimal amount, Long userId) {
        // 1. 参数校验
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.fail("充值金额必须大于0");
        }

        // 2. 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }

        // 3. 记录充值前的余额
        BigDecimal balanceBefore = user.getBalance();

        // 4. 计算充值后的余额
        BigDecimal newBalance = balanceBefore.add(amount);
        user.setBalance(newBalance);

        // 5. 更新用户余额
        int updateResult = userMapper.updateById(user);
        if (updateResult <= 0) {
            return Result.fail("充值失败");
        }

        // 6. 创建充值记录
        BalanceUsageRecord record = new BalanceUsageRecord();
        record.setPaymentId(null);  // 充值没有关联的支付ID
        record.setUserId(userId);
        record.setBalanceBefore(balanceBefore);
        record.setBalanceAfter(newBalance);
        record.setBalanceUsed(amount);  // 充值金额
        record.setTransactionType("充值");
        record.setHidden(0);

        int insertResult = balanceUsageRecordMapper.insert(record);
        if (insertResult <= 0) {
            // 如果记录插入失败，由于 @Transactional 注解会自动回滚
            throw new RuntimeException("创建充值记录失败");
        }

        return Result.success(true);
    }
}
