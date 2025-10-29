package com.itheima.simpleShoppingMallDemo.ModelVO;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CartItemVO {

    private Long cartItemId;
    private Long userId;
    private Long productId;
    private String name;
    private Integer quantity;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer hidden;
}
