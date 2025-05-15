package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/ifRepeat")
    public int selUserByName(@RequestParam("username") String username) {
        return userService.isUsernameExist(username) ? 1 : 0;
    }
    @PostMapping("/verify")
    public int selUserByNameAndPwd(@RequestBody User user){
        return userService.verifyUsernameAndPassword(user.getUsername(), user.getPassword()) ? 1 : 0;
    }

    @PostMapping("/register")
    public int regUserByNameAndPwd(@RequestBody User user){
        return userService.registerUser(user) ? 1 : 0;
    }
}
