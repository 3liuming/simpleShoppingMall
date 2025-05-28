package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.util.List;

@Data
public class BuyCartRequest {
    private List<Long> cartItemIds;
}
