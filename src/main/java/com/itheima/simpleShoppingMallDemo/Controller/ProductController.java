package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Service.BuyService;
import com.itheima.simpleShoppingMallDemo.Service.ProductService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    BuyService buyService;

    @GetMapping("/show")
    public Result<Product> selProductByProductId(@RequestParam("productId") Long productId){
        return productService.selProductByProductId(productId);
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
