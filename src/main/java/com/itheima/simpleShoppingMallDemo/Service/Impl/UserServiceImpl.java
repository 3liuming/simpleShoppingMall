package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.UserMapper;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Service.UserService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service("UserService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public Result<Boolean> isUsernameExist(String username) {
        return Result.success((lambdaQuery()
                .eq(User::getUsername, username)
                .count() > 0));
    }

    @Override
    public Result<Boolean> verifyUsernameAndPassword(String username, String password) {
        return Result.success(lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .count() > 0);
    }

    @Override
    public Result<Boolean> registerUser(User user) {
        if (isUsernameExist(user.getUsername()).getData()) {
            return Result.fail("用户名重复");
        }
        return Result.success(save(user));
    }
}