package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.UserFavoriteProduct;

import java.util.List;

public interface UserFavoriteProductService extends IService<UserFavoriteProduct> {
    /**
     * 新增商品收藏
     */
    boolean addFavorite(Long userId, Long productId);

    /**
     * 删除商品收藏
     */
    boolean removeFavorite(Long favoriteId);

    /**
     * 查询所有收藏
     */
    List<UserFavoriteProduct> getAllFavorites();

    /**
     * 根据userId查询收藏商品
     */
    List<UserFavoriteProduct> getFavoritesByUserId(Long userId);
}
