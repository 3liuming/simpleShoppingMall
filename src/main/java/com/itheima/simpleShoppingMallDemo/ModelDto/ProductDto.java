package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;

// ==================== 商品DTO ====================
@Data
public class ProductDto {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String productUrl;
    private String description;
    private Integer stock;
}
