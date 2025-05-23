package com.itheima.simpleShoppingMallDemo.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.simpleShoppingMallDemo.Model.Category;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Service.HomeService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                          @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer perPage){
        return Result.success(homeService.selProducts(page,perPage).getData());
    }

    @GetMapping("/bycategory")
    public Result<IPage<Product>> getProductByCategoyrId(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                 @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer perPage,
                                                 @RequestParam(name = "categoryId", required = false, defaultValue = "1") Long categoryId){
        return Result.success(homeService.selProductsByCategoryId(page,perPage,categoryId).getData());
    }
}
