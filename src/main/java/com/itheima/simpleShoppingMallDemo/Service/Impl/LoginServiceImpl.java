package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.UserMapper;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Service.LoginService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("loginService")
public class LoginServiceImpl extends ServiceImpl<UserMapper, User>
        implements LoginService {

    @Override
    public Result<Boolean> isUsernameExist(String username) {
        return Result.success((lambdaQuery()
                .eq(User::getUsername, username)
                .count() > 0));
    }

    @Override
    public Result<Long> verifyUsernameAndPassword(String username, String password) {
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .one();

        if (user != null) {
            return Result.success(user.getUserId()); // 登录成功，返回 userId
        } else {
            return Result.fail("用户名或密码错误"); // 登录失败
        }
    }

    @Override
    public Result<Boolean> registerUser(User user) {
        if (isUsernameExist(user.getUsername()).getData() != null) {
            return Result.fail("用户名重复");
        }
        return Result.success(save(user));
    }
}