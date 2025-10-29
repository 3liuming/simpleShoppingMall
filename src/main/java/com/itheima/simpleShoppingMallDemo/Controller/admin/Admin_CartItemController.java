package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.CartItem;
import com.itheima.simpleShoppingMallDemo.ModelVO.CartItemVO;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_CartItemService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台购物车管理控制器
 */
@RestController
@RequestMapping("/admin/cart")
public class Admin_CartItemController {

    @Autowired
    private Admin_CartItemService cartItemService;

    /**
     * 分页查询购物车列表
     */
    @GetMapping("/page")
    public Result<IPage<CartItemVO>> getCartItemPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long productId) {
        Page<CartItemVO> page = new Page<>(current, size);
        IPage<CartItemVO> result = cartItemService.getCartItemPage(page, userId, productId);
        return Result.success(result);
    }

    /**
     * 根据ID查询购物车详情
     */
    @GetMapping("/{id}")
    public Result<CartItem> getCartItemById(@PathVariable Long id) {
        CartItem cartItem = cartItemService.getCartItemById(id);
        return Result.success(cartItem);
    }

    /**
     * 新增购物车项(需要选择用户和商品)
     */
    @PostMapping
    public Result<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        CartItem result = cartItemService.addCartItem(cartItem);
        return Result.success(result);
    }

    /**
     * 更新购物车项
     */
    @PutMapping
    public Result<CartItem> updateCartItem(@RequestBody CartItem cartItem) {
        CartItem result = cartItemService.updateCartItem(cartItem);
        return Result.success(result);
    }

    /**
     * 删除购物车项
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return Result.success();
    }
}

