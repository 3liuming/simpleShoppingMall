package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;

// ==================== 订单项DTO ====================
@Data
public class OrderItemDto {
    private Long orderItemId;
    private Long productId;
    private String productName;
    private String productUrl;
    private Integer quantity;
    private BigDecimal price;
}
