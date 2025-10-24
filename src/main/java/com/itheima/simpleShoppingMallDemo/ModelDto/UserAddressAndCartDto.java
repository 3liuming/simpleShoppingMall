package com.itheima.simpleShoppingMallDemo.ModelDto;

import com.itheima.simpleShoppingMallDemo.Model.Address;
import lombok.Data;

import java.util.List;

@Data
public class UserAddressAndCartDto {
    private List<Address> addressList;
    private List<CartItemProductDto> cartItems;
}