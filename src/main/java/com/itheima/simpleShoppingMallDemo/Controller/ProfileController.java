package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.UserDto;
import com.itheima.simpleShoppingMallDemo.Service.ProfileService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @GetMapping("/userinfo")
    public Result<UserDto> selUserByUsername(HttpServletRequest request){
        String username = (String)request.getAttribute("username");
        return Result.success(profileService.selUserByUsername(username).getData());
    }

    @PutMapping("/update")
    public Result<Integer> updateByUser(@RequestBody UserDto userDto){
        System.out.println("收到更新请求：" + userDto);
        return Result.success(profileService.updateByUser(userDto).getData());
    }
}
