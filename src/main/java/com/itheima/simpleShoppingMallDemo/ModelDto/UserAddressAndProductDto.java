package com.itheima.simpleShoppingMallDemo.ModelDto;

import com.itheima.simpleShoppingMallDemo.Model.Address;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UserAddressAndProductDto {
    private List<Address> addressList;
    private String name;
    private BigDecimal price;
    private String productUrl;
}
