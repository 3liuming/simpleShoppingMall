package com.itheima.simpleShoppingMallDemo.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.simpleShoppingMallDemo.Model.CartProductDto;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 根据 userId 联表查询该用户购物车里的所有商品
     */
    @Select("""
            SELECT
            p.product_id,
            p.name,
            p.description,
            p.price,
            p.product_url,
            p.stock,
            c.quantity
            FROM products p
            INNER JOIN cart_items c
              ON p.product_id = c.product_id
            WHERE c.user_id = #{userId}
            """)
    List<CartProductDto> selectByUserId(@Param("userId") Long userId);
}
