package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.UserDto;
import com.itheima.simpleShoppingMallDemo.Service.ProfileService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @GetMapping("/userinfo")
    public Result<UserDto> selUserByUsername(@RequestParam("username") String username){
        return Result.success(profileService.selUserByUsername(username).getData());
    }

    @PutMapping("/update")
    public Result<Integer> updateByUser(@RequestBody UserDto userDto){
        System.out.println("收到更新请求：" + userDto);
        return Result.success(profileService.updateByUser(userDto).getData());
    }
}
