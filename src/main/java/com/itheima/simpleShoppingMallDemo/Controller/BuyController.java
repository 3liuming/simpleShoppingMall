package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserProductDto;
import com.itheima.simpleShoppingMallDemo.Service.BuyService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buy")
public class BuyController {
    @Autowired
    BuyService buyService;

    @GetMapping("/list")
    public Result<UserProductDto> selUserProductByUidAndPid(@RequestParam("productId") Long productId,
                                                            HttpServletRequest request){
        return buyService.selUserProductByUidAndPid((Long)request.getAttribute("userId"),productId);
    }

    @PostMapping("/create")
    public Result<Boolean> createOrderByUsernameAndQuantityAndPid(@RequestBody OrderItem orderItem,
                                                                  HttpServletRequest request){
        return buyService.createOrderByUsernameAndQuantityAndPid((Long)request.getAttribute("userId"),orderItem);
    }

    @PostMapping("/nowbuy")
    public Result<Boolean> CreatePaymentByUserNameAndQuantityAndPid(@RequestBody OrderItem orderItem,
                                                                    HttpServletRequest request){
        return buyService.createPaymentByUsernameAndQuantityAndPid((Long)request.getAttribute("userId"),orderItem);
    }
}
