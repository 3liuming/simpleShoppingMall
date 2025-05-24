package com.itheima.simpleShoppingMallDemo.Model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String address;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
