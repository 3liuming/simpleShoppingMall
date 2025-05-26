package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserProductDto {
    private String username;
    private String phone;
    private String address;
    private String name;
    private BigDecimal price;
    private String productUrl;
}
