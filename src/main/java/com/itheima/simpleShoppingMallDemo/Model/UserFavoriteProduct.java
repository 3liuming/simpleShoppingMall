package com.itheima.simpleShoppingMallDemo.Model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("user_favorite_product")
public class UserFavoriteProduct {
    @TableId(value = "favorite_id", type = IdType.AUTO)
    private Long favoriteId;
    private Long userId;
    private Long productId;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;
//    @TableLogic@TableField("hidden")
    private Integer hidden;
}
