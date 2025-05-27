package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.OrderMapper;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.ModelDto.OrderDto;
import com.itheima.simpleShoppingMallDemo.Service.OrderService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service("OrderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    OrderMapper orderMapper;

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
    public Result<Boolean> createPaymentByUserIdAndOrderId(Long userId,Long OrderId){
        return Result.success();
    }

    @Override
    public Result<Boolean> deleteOrderByOrderId(Long OrderId){
        return Result.success();
    }
}
