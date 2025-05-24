package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Service.LoginService;
import com.itheima.simpleShoppingMallDemo.common.JwtTokenUtil;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("/ifRepeat")
    public Result<Integer> selUserByName(@RequestParam("username") String username) {
        return Result.success(loginService.isUsernameExist(username).getData() ? 1 : 0);
    }
    @PostMapping("/verify")
    public Result<String> selUserByNameAndPwd(@RequestBody User user){
        Long userId = loginService.verifyUsernameAndPassword(user.getUsername(), user.getPassword()).getData();
        if(userId != null){
            return Result.success(jwtTokenUtil.generateToken(user.getUsername(), userId));
        }else {
            return Result.fail("注册失败");
        }
    }

    @PostMapping("/register")
    public Result<Integer> regUserByNameAndPwd(@RequestBody User user){
        return Result.success(loginService.registerUser(user).getData() ? 1 : 0);
    }
}
