package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.ModelDto.OrderDto;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService extends IService<Order> {

    Result<List<OrderDto>> selAllOrderByUserId(Long userId);

    Result<List<OrderDto>> selPaidOrderByUserId(Long userId);

    Result<List<OrderDto>> selUnpaidOrderByUserId(Long userId);

    Result<Boolean> createPaymentByUserIdAndOrderId(Long OrderId);

    Result<Boolean> deleteOrderByOrderId(Long OrderId);
}
