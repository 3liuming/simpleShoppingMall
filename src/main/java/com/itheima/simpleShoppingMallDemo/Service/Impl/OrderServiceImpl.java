package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
    @Autowired
    BalanceUsageRecordMapper balanceUsageRecordMapper;
    @Autowired
    private ShipmentMapper shipmentMapper;

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
    public Result<Boolean> createPaymentByUserIdAndOrderId(Long orderId) {
        BalanceUsageRecord balanceUsageRecord = new BalanceUsageRecord();

        // 1. 查询订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 2. 检查订单状态
        if (order.getStatus() != null && order.getStatus().equals(1L)) {
            throw new RuntimeException("该订单已支付，不能重复支付");
        }

        // 3. 查询订单明细（这里只查一个用于验证，实际上可能有多个）
        OrderItem orderItem = orderItemMapper.selectOne(
                new LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, orderId)
                        .last("LIMIT 1")
        );

        if (orderItem == null) {
            throw new RuntimeException("订单明细不存在");
        }

        // 4. 查询用户信息
        User user = userMapper.selectById(order.getUserId());

        // 5. 验证余额
        if (user.getBalance() == null || order.getTotalPrice() == null
                || user.getBalance().compareTo(BigDecimal.ZERO) <= 0
                || order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0
                || user.getBalance().compareTo(order.getTotalPrice()) < 0) {
            throw new RuntimeException("余额不足，或值为空/无效");
        }

        // 6. 计算支付后余额
        BigDecimal endBalance = user.getBalance().subtract(order.getTotalPrice());

        // 7. 创建支付记录
        Payment payment = new Payment();
        payment.setStatus(1L);
        payment.setUserId(order.getUserId());
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalPrice());

        int resP = paymentMapper.insert(payment);
        if (resP <= 0) {
            throw new RuntimeException("支付明细插入失败");
        }

        // ✨✨✨ 8. 更新发货表的 paymentId（新增部分）✨✨✨
        LambdaUpdateWrapper<Shipment> shipmentUpdateWrapper = Wrappers.lambdaUpdate();
        shipmentUpdateWrapper
                .set(Shipment::getPaymentId, payment.getPaymentId())
                .eq(Shipment::getOrderId, orderId);

        int shipmentUpdateResult = shipmentMapper.update(shipmentUpdateWrapper);
        if (shipmentUpdateResult <= 0) {
            throw new RuntimeException("发货表 paymentId 更新失败");
        }
        // ✨✨✨ 更新发货表结束 ✨✨✨

        // 9. 更新用户余额
        LambdaUpdateWrapper<User> updateWrapper1 = Wrappers.lambdaUpdate();
        updateWrapper1
                .set(User::getBalance, endBalance)
                .eq(User::getUserId, order.getUserId());

        int resU = userMapper.update(updateWrapper1);
        if (resU <= 0) {
            throw new RuntimeException("用户余额更新失败");
        }

        // 10. 更新订单状态
        LambdaUpdateWrapper<Order> updateWrapper2 = Wrappers.lambdaUpdate();
        updateWrapper2
                .set(Order::getStatus, 1L)
                .eq(Order::getOrderId, orderId);

        int resO = orderMapper.update(updateWrapper2);
        if (resO <= 0) {
            throw new RuntimeException("订单状态更新失败");
        }

        // 11. 创建余额使用记录
        balanceUsageRecord.setUserId(payment.getUserId());
        balanceUsageRecord.setPaymentId(payment.getPaymentId());
        balanceUsageRecord.setBalanceBefore(user.getBalance());
        balanceUsageRecord.setBalanceUsed(order.getTotalPrice());
        balanceUsageRecord.setBalanceAfter(endBalance);
        balanceUsageRecord.setTransactionType("消费");

        int resR = balanceUsageRecordMapper.insert(balanceUsageRecord);
        if (resR <= 0) {
            throw new RuntimeException("余额使用记录插入失败");
        }

        return Result.success(true);
    }

    @Override
    @Transactional
    public Result<Boolean> deleteOrderByOrderId(Long orderId){
        LambdaUpdateWrapper<Order> updateWrapper1 = Wrappers.lambdaUpdate();
        OrderItem orderItem = orderItemMapper.selectById(orderId);//通过orderid获取订单项id
        Product product = productMapper.selectById(orderItem.getProductId());//通过商品id获取商品
        Integer stock  = product.getStock()+orderItem.getQuantity();//确定更新后的数量

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

        UpdateWrapper<Product> updateWrapper3 = new UpdateWrapper<>();
        updateWrapper3.eq("product_id", orderItem.getProductId());//设置要更新的主键
        product.setStock(stock);
        int res3 = productMapper.update(product,updateWrapper3);//更新库存
        if (res3 <=0){
            throw new RuntimeException("库存更新失败");
        }
        return Result.success(true);
    }
}
