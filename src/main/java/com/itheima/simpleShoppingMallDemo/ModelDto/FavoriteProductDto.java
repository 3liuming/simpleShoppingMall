package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// ==================== 收藏商品DTO ====================
@Data
public class FavoriteProductDto {
    private Long favoriteId;
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productUrl;
    private Timestamp createdAt;
}