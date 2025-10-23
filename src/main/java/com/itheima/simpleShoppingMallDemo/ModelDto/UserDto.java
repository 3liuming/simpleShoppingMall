package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class UserDto {
    private Long userId;
    private String nickname;
    private String username;
    private String email;
    private String phone;
    private String address;
    private BigDecimal balance;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
