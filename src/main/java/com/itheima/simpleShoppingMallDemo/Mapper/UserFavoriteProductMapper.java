package com.itheima.simpleShoppingMallDemo.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.simpleShoppingMallDemo.Model.UserFavoriteProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserFavoriteProductMapper extends BaseMapper<UserFavoriteProduct> {

    @Select("SELECT * FROM user_favorite_product " +
            "WHERE user_id = #{userId} AND product_id = #{productId} " +
            "LIMIT 1")
    UserFavoriteProduct selectIncludeDeleted(@Param("userId") Long userId,
                                             @Param("productId") Long productId);

    @Update("UPDATE user_favorite_product " +
            "SET hidden = 0, created_at = NOW() " +
            "WHERE favorite_id = #{favoriteId}")
    int restoreFavorite(@Param("favoriteId") Long favoriteId);
}
