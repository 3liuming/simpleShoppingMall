package com.itheima.simpleShoppingMallDemo.Model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("cart_items")
public class CartItem {
    @TableId(value = "cart_item_id",type = IdType.AUTO)
    private Long cartItemId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updatedAt;
}

