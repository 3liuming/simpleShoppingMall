package com.itheima.simpleShoppingMallDemo.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.simpleShoppingMallDemo.Model.Category;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Service.HomeService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    HomeService homeService;

    @GetMapping("/catlist")
    public Result<List<Category>> getList(){
        return Result.success(homeService.selCategories().getData());
    }

    @GetMapping("/prolist")
    public Result<IPage<Product>> getList(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer perPage,
                                          @RequestParam(name = "sort", required = false, defaultValue = "default") String sort){
        return Result.success(homeService.selProducts(page, perPage, sort).getData());
    }

    @GetMapping("/bycategory")
    public Result<IPage<Product>> getProductByCategoyrId(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                         @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer perPage,
                                                         @RequestParam(name = "categoryId", required = false, defaultValue = "1") Long categoryId,
                                                         @RequestParam(name = "sort", required = false, defaultValue = "default") String sort){
        return Result.success(homeService.selProductsByCategoryId(page, perPage, categoryId, sort).getData());
    }

    @GetMapping("/search")
    public Result<IPage<Product>> searchProducts(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                 @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer perPage,
                                                 @RequestParam(name = "keyword", required = true) String keyword,
                                                 @RequestParam(name = "sort", required = false, defaultValue = "default") String sort){
        return Result.success(homeService.searchProducts(page, perPage, keyword, sort).getData());
    }

    @PostMapping("/addcart")
    public Result<Boolean> addCartWithPidAndUid(@RequestParam("productId") Long productId, HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        System.out.println(productId);
        return homeService.addCartWithPidAndUid(userId, productId);
    }
}

