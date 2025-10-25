package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.ModelDto.LoginResult;
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
    public Result<LoginResult> selUserByNameAndPwd(@RequestBody User user){
        User resuser = loginService.verifyUsernameAndPassword(user.getUsername(), user.getPassword()).getData();
        if(resuser.getUserId() != null){
            LoginResult result = new LoginResult();
            result.setToken(jwtTokenUtil.generateToken(resuser.getUsername(), resuser.getUserId(), resuser.getGrade()));
            result.setGrade(resuser.getGrade());
            result.setUserName(resuser.getUsername());
            result.setUserId(resuser.getUserId());
            return Result.success(result);
        }else {
            return Result.fail("登录失败");
        }
    }

    @PostMapping("/register")
    public Result<Integer> regUserByNameAndPwd(@RequestBody User user) {
        // 1. 调用服务层方法，获取注册结果
        Result<Boolean> registerResult = loginService.registerUser(user);

        // 2. 判断注册结果是否成功（假设Result有isSuccess()方法，或通过code判断）
        if (registerResult.isSuccess()) {
            Boolean data = registerResult.getData();
            // 3. 非空校验：若data为null，默认返回0（失败）
            int resultCode = (data != null && data) ? 1 : 0;
            return Result.success(resultCode);
        } else {
            // 4. 注册失败，直接返回错误信息（复用原失败结果的code和message）
            return Result.fail(registerResult.getMessage());
        }
    }
}
