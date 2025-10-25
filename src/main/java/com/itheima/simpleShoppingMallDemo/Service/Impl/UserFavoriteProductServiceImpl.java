package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.UserFavoriteProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.UserFavoriteProduct;
import com.itheima.simpleShoppingMallDemo.Service.UserFavoriteProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavoriteProductServiceImpl extends ServiceImpl<UserFavoriteProductMapper, UserFavoriteProduct>
        implements UserFavoriteProductService {

    @Override
    public boolean addFavorite(Long userId, Long productId) {
        // 检查是否已经收藏
        LambdaQueryWrapper<UserFavoriteProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoriteProduct::getUserId, userId)
                .eq(UserFavoriteProduct::getProductId, productId);

        UserFavoriteProduct existing = this.getOne(wrapper);
        if (existing != null) {
            throw new RuntimeException("该商品已收藏");
        }

        UserFavoriteProduct favorite = new UserFavoriteProduct();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        return this.save(favorite);
    }

    @Override
    public boolean removeFavorite(Long favoriteId) {
        return this.removeById(favoriteId);
    }

    @Override
    public List<UserFavoriteProduct> getAllFavorites() {
        return this.list();
    }

    @Override
    public List<UserFavoriteProduct> getFavoritesByUserId(Long userId) {
        LambdaQueryWrapper<UserFavoriteProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoriteProduct::getUserId, userId)
                .orderByDesc(UserFavoriteProduct::getCreatedAt);
        return this.list(wrapper);
    }
}