package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.UserFavoriteProduct;
import com.itheima.simpleShoppingMallDemo.Service.UserFavoriteProductService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 4. Controller控制器
@RestController
@RequestMapping("/favorite")
public class UserFavoriteProductController {

    @Autowired
    private UserFavoriteProductService favoriteService;

    /**
     * 新增商品收藏
     */
    @PostMapping("/add")
    public Result addFavorite(@RequestParam("productId") Long productId,
                              HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            boolean success = favoriteService.addFavorite(userId, productId);
            return success ? Result.success("收藏成功") : Result.fail("收藏失败");
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 删除商品收藏
     */
    @DeleteMapping("/remove/{favoriteId}")
    public Result removeFavorite(@PathVariable Long favoriteId) {
        boolean success = favoriteService.removeFavorite(favoriteId);
        return success ? Result.success("取消收藏成功") : Result.fail("取消收藏失败");
    }

    /**
     * 查询所有收藏
     */
    @GetMapping("/all")
    public Result getAllFavorites() {
        List<UserFavoriteProduct> favorites = favoriteService.getAllFavorites();
        return Result.success(favorites);
    }

    /**
     * 根据userId查询收藏商品
     */
    @GetMapping("/user")
    public Result getFavoritesByUserId(HttpServletRequest request) {
        List<UserFavoriteProduct> favorites = favoriteService.getFavoritesByUserId((Long) request.getAttribute("userId"));
        return Result.success(favorites);
    }
}
