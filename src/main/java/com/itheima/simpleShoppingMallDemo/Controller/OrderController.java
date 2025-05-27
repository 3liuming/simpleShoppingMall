package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.ModelDto.OrderDto;
import com.itheima.simpleShoppingMallDemo.Service.OrderService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @GetMapping("/all")
    public Result<List<OrderDto>> selAllOrderByUserId(HttpServletRequest request){
        List<OrderDto> orderDtos = orderService.selAllOrderByUserId((Long) request.getAttribute("userId")).getData();

        if (orderDtos != null){
            return Result.success(orderDtos);
        }else {
            return Result.fail("返回结果为空");
        }
    }
    @GetMapping("/paid")
    public Result<List<OrderDto>> selPaidOrderByUserId(HttpServletRequest request){
        List<OrderDto> orderDtos = orderService.selPaidOrderByUserId((Long) request.getAttribute("userId")).getData();

        if (orderDtos != null){
            return Result.success(orderDtos);
        }else {
            return Result.fail("返回结果为空");
        }
    }
    @GetMapping("/unpaid")
    public Result<List<OrderDto>> selUnpaidOrderByUserId(HttpServletRequest request){
        List<OrderDto> orderDtos = orderService.selUnpaidOrderByUserId((Long) request.getAttribute("userId")).getData();

        if (orderDtos != null){
            return Result.success(orderDtos);
        }else {
            return Result.fail("返回结果为空");
        }
    }

    @PostMapping("/buy")
    public Result<Boolean> createPaymentByUserIdAndOrderId(@RequestParam("orderId") Long orderId,
                                                           HttpServletRequest request){
        return orderService.createPaymentByUserIdAndOrderId((Long) request.getAttribute("userId"),orderId);
    }

    @PostMapping("/delete")
    public Result<Boolean> deleteOrderByOrderId(@RequestParam("orderId") Long orderId){
        return orderService.deleteOrderByOrderId(orderId);
    }
}
