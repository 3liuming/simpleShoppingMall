package com.itheima.simpleShoppingMallDemo.ModelVO;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserFavoriteProductVO {
    private Long favoriteId;
    private Long userId;
    private Long productId;
    private String name;
    private Timestamp createdAt;
    private Integer hidden;
}
