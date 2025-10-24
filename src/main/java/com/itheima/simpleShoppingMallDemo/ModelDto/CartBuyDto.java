package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartBuyDto {
    private Long orderItemId;
    private Long orderId;
    private BigDecimal price;
    private Long addressId;           // 收货地址ID（必传）
    private Long productId;           // 单品购买时使用
    private Integer quantity;         // 单品购买时使用
    private List<Long> cartItemIds;   // 购物车结算时使用
}
