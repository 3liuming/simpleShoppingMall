package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDto {
    private Long productId;
    private Long cartItemId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String productUrl;
    private Integer quantity;
}
