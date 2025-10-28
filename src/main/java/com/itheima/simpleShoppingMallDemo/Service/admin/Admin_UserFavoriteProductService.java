package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.UserFavoriteProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.UserFavoriteProduct;
import org.springframework.stereotype.Service;

/**
 * 后台用户收藏管理服务
 */
@Service
public class Admin_UserFavoriteProductService extends ServiceImpl<UserFavoriteProductMapper, UserFavoriteProduct> {

    /**
     * 分页查询收藏
     */
    public IPage<UserFavoriteProduct> getFavoritePage(Page<UserFavoriteProduct> page, Long userId, Long productId) {
        LambdaQueryWrapper<UserFavoriteProduct> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(UserFavoriteProduct::getUserId, userId);
        }
        if (productId != null) {
            wrapper.eq(UserFavoriteProduct::getProductId, productId);
        }
        wrapper.orderByDesc(UserFavoriteProduct::getCreatedAt);
        return this.page(page, wrapper);
    }

    /**
     * 根据ID查询收藏
     */
    public UserFavoriteProduct getFavoriteById(Long id) {
        return this.getById(id);
    }

    /**
     * 删除收藏
     */
    public void deleteFavorite(Long id) {
        this.removeById(id);
    }
}