package com.itheima.simpleShoppingMallDemo.Service;

import com.itheima.simpleShoppingMallDemo.Model.UserDto;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;

@Service
public interface ProfileService {

    Result<UserDto> selUserByUsername(String username);

    Result<Integer> updateByUser(UserDto userDto);
}
