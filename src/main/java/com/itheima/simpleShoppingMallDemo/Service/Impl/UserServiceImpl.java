package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.UserMapper;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service("UserService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public boolean isUsernameExist(String username) {
        return lambdaQuery()
                .eq(User::getUsername, username)
                .count() > 0;
    }

    @Override
    public boolean verifyUsernameAndPassword(String username, String password) {
        return lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .count() > 0;
    }

    @Override
    public boolean registerUser(User user) {
        if (isUsernameExist(user.getUsername())) {
            return false;
        }
        return save(user);
    }
}