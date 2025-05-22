package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Service.UserService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/ifRepeat")
    public Result<Integer> selUserByName(@RequestParam("username") String username) {
        return Result.success(userService.isUsernameExist(username).getData() ? 1 : 0);
    }
    @PostMapping("/verify")
    public Result<Integer> selUserByNameAndPwd(@RequestBody User user){
        return Result.success(userService.verifyUsernameAndPassword(user.getUsername(), user.getPassword()).getData() ? 1 : 0);
    }

    @PostMapping("/register")
    public Result<Integer> regUserByNameAndPwd(@RequestBody User user){
        return Result.success(userService.registerUser(user).getData() ? 1 : 0);
    }
}
