package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.UserFavoriteProduct;
import com.itheima.simpleShoppingMallDemo.ModelVO.UserFavoriteProductVO;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_UserFavoriteProductService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台用户收藏管理控制器
 */
@RestController
@RequestMapping("/admin/favorite")
public class Admin_UserFavoriteProductController {

    @Autowired
    private Admin_UserFavoriteProductService favoriteService;

    /**
     * 分页查询收藏列表(关联用户信息)
     */
    @GetMapping("/page")
    public Result<IPage<UserFavoriteProductVO>> getFavoritePage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long productId) {
        Page<UserFavoriteProductVO> page = new Page<>(current, size);
        IPage<UserFavoriteProductVO> result = favoriteService.getFavoritePage(page, userId, productId);
        return Result.success(result);
    }

    /**
     * 根据ID查询收藏详情
     */
    @GetMapping("/{id}")
    public Result<UserFavoriteProduct> getFavoriteById(@PathVariable Long id) {
        UserFavoriteProduct favorite = favoriteService.getFavoriteById(id);
        return Result.success(favorite);
    }

    /**
     * 删除收藏
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return Result.success();
    }
}
