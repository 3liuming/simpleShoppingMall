package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.ModelDto.BuyCartRequest;
import com.itheima.simpleShoppingMallDemo.ModelDto.CartNumRequest;
import com.itheima.simpleShoppingMallDemo.ModelDto.CartProductDto;
import com.itheima.simpleShoppingMallDemo.Service.CartService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/buy")
    public Result<Boolean> createPaymentByCartItemId(HttpServletRequest request,
                                                     @RequestBody BuyCartRequest buyCartRequest){
        return cartService.createPaymentByCartItemId((Long) request.getAttribute("userId"),buyCartRequest.getCartItemIds());
    }


    @PatchMapping("/num")
    public Result<Boolean> updateCartWithQuantityByNum(@RequestBody CartNumRequest cartNumRequest){
        return cartService.updateCartWithQuantityByNum(cartNumRequest);
    }
    @DeleteMapping("/delete")
    public Result<Boolean> deleteCartByCartItemId(@RequestParam("cartItemId") Long cartItemId,
                                                  HttpServletRequest request){
        return cartService.deleteCartByCartItemId((Long) request.getAttribute("userId"),cartItemId);
    }
}
