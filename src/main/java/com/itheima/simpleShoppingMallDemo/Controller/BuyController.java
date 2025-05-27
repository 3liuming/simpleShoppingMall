package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserProductDto;
import com.itheima.simpleShoppingMallDemo.Service.BuyService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
