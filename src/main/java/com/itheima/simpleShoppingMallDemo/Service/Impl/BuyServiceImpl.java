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
    @Override
    public Result<UserProductDto> selUserProductByUidAndPid(Long userId,Long productId){
        if (userId == null || productId == null){
            return Result.fail("值为空");
        }
        User user = userMapper.selectById(userId);
        Product product = productMapper.selectById(productId);

        if (user != null && product != null){
            UserProductDto userProductDto = new UserProductDto();

            userProductDto.setUsername(user.getUsername());
            userProductDto.setPhone(user.getPhone());
            userProductDto.setAddress(user.getAddress());

            userProductDto.setName(product.getName());
            userProductDto.setPrice(product.getPrice());
            userProductDto.setProductUrl(product.getProductUrl());
            return Result.success(userProductDto);
        }else {
            return Result.fail("查询失败");
        }
    }

    @Override
    @Transactional
    public Result<Boolean> createOrderByUsernameAndQuantityAndPid(Long userId, OrderItem orderItem){
        Product product = productMapper.selectById(orderItem.getProductId());
        Order order = new Order();
        if (userId == null || orderItem.getQuantity() == null || product.getPrice() == null){
            System.out.println(userId);
            System.out.println(orderItem.getQuantity());
            System.out.println(product.getPrice());
            return Result.fail("quantity或price其中值为空");
        }
        order.setUserId(userId);
        order.setTotalPrice(product.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        order.setStatus(0L);
        save(order);
        if(order.getOrderId() == null){
            throw new RuntimeException("创建订单失败，orderId 为空");
        }
        orderItem.setOrderId(order.getOrderId());
        orderItem.setPrice(product.getPrice());
        int row = orderItemMapper.insert(orderItem);
        if (row <= 0){
            throw new RuntimeException("订单明细插入失败");
        }
        return Result.success(true);
    }

    @Override
    @Transactional
    public Result<Boolean> createPaymentByUsernameAndQuantityAndPid(Long userId, OrderItem orderItem){

        Product product = productMapper.selectById(orderItem.getProductId());
        Order order = new Order();
        if (userId == null || orderItem.getQuantity() == null || product.getPrice() == null){
            return Result.fail("quantity或price其中值为空");
        }
        order.setUserId(userId);
        order.setTotalPrice(product.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        order.setStatus(1L);
        save(order);
        if(order.getOrderId() == null){
            throw new RuntimeException("创建订单失败，orderId 为空");
        }
        orderItem.setOrderId(order.getOrderId());
        orderItem.setPrice(product.getPrice());
        int row = orderItemMapper.insert(orderItem);
        if (row <= 0){
            throw new RuntimeException("订单明细插入失败");
        }

        User user = userMapper.selectById(userId);
        if (user.getBalance() == null || order.getTotalPrice() == null
                || user.getBalance().compareTo(BigDecimal.ZERO) <= 0
                || order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0
                || user.getBalance().compareTo(order.getTotalPrice()) < 0) {

            throw new RuntimeException("余额不足，或值为空/无效");
        }

        Payment payment = new Payment();
        BigDecimal endBalance = user.getBalance().subtract(order.getTotalPrice());

        payment.setUserId(userId);
        payment.setOrderId(order.getOrderId());
        payment.setAmount(order.getTotalPrice());
        payment.setStatus(1L);

        int res = paymentMapper.insert(payment);
        if (res <= 0){
            throw new RuntimeException("支付明细插入失败");
        }

        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper
                .set(User::getBalance, endBalance)
                .eq(User::getUserId,userId);  // 指定哪个用户
        int success = userMapper.update(updateWrapper);
        if (success <=0){
            throw new RuntimeException("用户余额更新失败");
        }
        return Result.success();
    }
}
