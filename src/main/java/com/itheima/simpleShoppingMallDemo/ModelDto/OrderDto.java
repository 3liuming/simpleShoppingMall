package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDto {
    private Long orderItemId;
    private Long orderId;
    private Long status;
    private Long productId;
    private String productName;
    private String ProductImage;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}
