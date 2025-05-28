package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.ModelDto.CartProductDto;
import com.itheima.simpleShoppingMallDemo.Service.CartService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/list")
    public Result<List<CartProductDto>> selProductByUserId(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        return cartService.selectByUserId(userId);
    }
}
