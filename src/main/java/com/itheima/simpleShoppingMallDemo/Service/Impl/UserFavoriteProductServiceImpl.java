package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.UserFavoriteProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.UserFavoriteProduct;
import com.itheima.simpleShoppingMallDemo.Service.UserFavoriteProductService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserFavoriteProductServiceImpl extends ServiceImpl<UserFavoriteProductMapper, UserFavoriteProduct>
        implements UserFavoriteProductService {

    @Override
    public boolean addFavorite(Long userId, Long productId) {
        // 使用自定义方法查询,包含逻辑删除的记录
        UserFavoriteProduct existing = this.baseMapper.selectIncludeDeleted(userId, productId);

        if (existing != null) {
            if (existing.getHidden() == 1) {
                // 逻辑删除的记录,恢复它
                // 方案1: 使用自定义 SQL 更新
                int result = this.baseMapper.restoreFavorite(existing.getFavoriteId());
                return result > 0;

            } else {
                // 正常记录,已收藏
                throw new RuntimeException("该商品已收藏");
            }
        }

        // 不存在则新增
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