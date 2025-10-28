package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CartItemMapper;
import com.itheima.simpleShoppingMallDemo.Model.CartItem;
import org.springframework.stereotype.Service;

/**
 * 后台购物车管理服务
 */
@Service
public class Admin_CartItemService extends ServiceImpl<CartItemMapper, CartItem> {

    /**
     * 分页查询购物车
     */
    public IPage<CartItem> getCartItemPage(Page<CartItem> page, Long userId, Long productId) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(CartItem::getUserId, userId);
        }
        if (productId != null) {
            wrapper.eq(CartItem::getProductId, productId);
        }
        wrapper.orderByDesc(CartItem::getCreatedAt);
        return this.page(page, wrapper);
    }

    /**
     * 根据ID查询购物车项
     */
    public CartItem getCartItemById(Long id) {
        return this.getById(id);
    }

    /**
     * 新增购物车项
     */
    public CartItem addCartItem(CartItem cartItem) {
        this.save(cartItem);
        return cartItem;
    }

    /**
     * 更新购物车项
     */
    public CartItem updateCartItem(CartItem cartItem) {
        this.updateById(cartItem);
        return cartItem;
    }

    /**
     * 删除购物车项
     */
    public void deleteCartItem(Long id) {
        this.removeById(id);
    }
}