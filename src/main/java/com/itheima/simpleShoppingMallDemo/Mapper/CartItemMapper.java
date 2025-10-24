package com.itheima.simpleShoppingMallDemo.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.simpleShoppingMallDemo.Model.CartItem;
import org.apache.ibatis.annotations.Param;

public interface CartItemMapper extends BaseMapper<CartItem> {
    /**
     * 根据用户ID和购物车项ID查询
     */
    CartItem selByUserIdAndCartItemId(@Param("userId") Long userId, @Param("cartItemId") Long cartItemId);
}
