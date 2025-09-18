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
    public Result<User> verifyUsernameAndPassword(String username, String password) {
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .one();

        if (user != null) {
            return Result.success(user); // 登录成功，返回 userId
        } else {
            return Result.fail("用户名或密码错误"); // 登录失败
        }
    }

    @Override
    public Result<Boolean> registerUser(User user) {
        // 1. 调用isUsernameExist获取用户名是否存在的结果
        Result<Boolean> existResult = isUsernameExist(user.getUsername());
        Boolean isExist = existResult.getData();

        // 2. 先判断data是否为null（避免isUsernameExist异常导致的null），再判断是否存在
        if (isExist != null && isExist) { // 用户名存在
            return Result.fail("用户名重复");
        }

        // 3. 用户名不存在，执行保存操作（save方法返回boolean：成功true/失败false）
        boolean saveSuccess = save(user);
        return Result.success(saveSuccess);
    }
}