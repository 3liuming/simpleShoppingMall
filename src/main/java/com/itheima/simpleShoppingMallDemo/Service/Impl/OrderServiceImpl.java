package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.*;
import com.itheima.simpleShoppingMallDemo.Model.*;
import com.itheima.simpleShoppingMallDemo.ModelDto.OrderDto;
import com.itheima.simpleShoppingMallDemo.Service.OrderService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service("OrderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ProductMapper productMapper;

    @Override
    public Result<List<OrderDto>> selAllOrderByUserId(Long userId){

        List<OrderDto> orderDtos = orderMapper.selAllOrderByUserId(userId);

        if (orderDtos != null){
            return Result.success(orderDtos);
        }else{
            return Result.fail("查询结果为空");
        }

    }

    @Override
    public Result<List<OrderDto>> selPaidOrderByUserId(Long userId){

        List<OrderDto> orderDtos = orderMapper.selPaidOrderByUserId(userId);

        if (orderDtos != null){
            return Result.success(orderDtos);
        }else{
            return Result.fail("查询结果为空");
        }
    }

    @Override
    public Result<List<OrderDto>> selUnpaidOrderByUserId(Long userId){

        List<OrderDto> orderDtos = orderMapper.selUnpaidOrderByUserId(userId);

        if (orderDtos != null){
            return Result.success(orderDtos);
        }else{
            return Result.fail("查询结果为空");
        }
    }

    @Override
    @Transactional
    public Result<Boolean> createPaymentByUserIdAndOrderId(Long orderId){
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != null && order.getStatus().equals(1L)) {
            throw new RuntimeException("该订单已支付，不能重复支付");
        }
        OrderItem orderItem = orderItemMapper.selectOne(
                new LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, orderId)
                        .last("LIMIT 1")
        );
        User user = userMapper.selectById(order.getUserId());
        Payment payment = new Payment();
        if(orderItem == null){
            throw new RuntimeException("订单明细不存在");
        }

        if (user.getBalance() == null || order.getTotalPrice() == null
                || user.getBalance().compareTo(BigDecimal.ZERO) <= 0
                || order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0
                || user.getBalance().compareTo(order.getTotalPrice()) < 0) {

            throw new RuntimeException("余额不足，或值为空/无效");
        }

        BigDecimal endBalance = user.getBalance().subtract(order.getTotalPrice());
        user.setBalance(endBalance);
        payment.setStatus(1L);
        payment.setUserId(order.getUserId());
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalPrice());


        LambdaUpdateWrapper<User> updateWrapper1 = Wrappers.lambdaUpdate();
        updateWrapper1
                .set(User::getBalance, endBalance)
                .eq(User::getUserId,order.getUserId());  // 指定哪个用户
        int resU = userMapper.update(updateWrapper1);
        if (resU <=0){
            throw new RuntimeException("用户余额更新失败");
        }

        LambdaUpdateWrapper<Order> updateWrapper2 = Wrappers.lambdaUpdate();
        updateWrapper2
                .set(Order::getStatus, 1L)
                .eq(Order::getOrderId, orderId);  // 指定哪个订单
        int resO = orderMapper.update(updateWrapper2);
        if (resO <=0){
            throw new RuntimeException("订单状态更新失败");
        }

        int resP = paymentMapper.insert(payment);
        if (resP <=0){
            throw new RuntimeException("支付明细插入失败");
        }

        Product product = productMapper.selectOne(
                new LambdaQueryWrapper<Product>()
                        .eq(Product::getProductId, orderItem.getProductId())
                        .last("LIMIT 1"));

        Integer stock = product.getStock() - orderItem.getQuantity();

        if (stock <= 0){
            throw new RuntimeException("库存不足");
        }
        LambdaUpdateWrapper<Product> updateWrapper3 = Wrappers.lambdaUpdate();
        updateWrapper3
                .set(Product::getStock, stock)
                .eq(Product::getProductId, orderItem.getProductId());
        int resp = productMapper.update(updateWrapper3);

        if (resp <= 0){
            throw new RuntimeException("库存更新失败");
        }
        return Result.success(true);
    }

    @Override
    @Transactional
    public Result<Boolean> deleteOrderByOrderId(Long orderId){
        LambdaUpdateWrapper<Order> updateWrapper1 = Wrappers.lambdaUpdate();
        updateWrapper1
                .set(Order::getHidden,1)
                .eq(Order::getOrderId,orderId);
        int res1 = orderMapper.update(updateWrapper1);
        if (res1 <=0){
            throw new RuntimeException("order表删除失败");
        }

        LambdaUpdateWrapper<OrderItem> updateWrapper2 = Wrappers.lambdaUpdate();
        updateWrapper2
                .set(OrderItem::getHidden,1)
                .eq(OrderItem::getOrderId,orderId);
        int res2 = orderItemMapper.update(updateWrapper2);

        if (res2 <=0){
            throw new RuntimeException("orderItem表删除失败");
        }
        return Result.success(true);
    }
}
