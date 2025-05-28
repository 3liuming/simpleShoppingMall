package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

@Data
public class CartNumRequest {
    private Long cartItemId;
    private Integer quantity;
    private Integer delta;
}
