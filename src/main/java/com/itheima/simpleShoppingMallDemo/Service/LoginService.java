package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;

@Service
public interface LoginService extends IService<User> {

    Result<Boolean> isUsernameExist(String username);

    Result<User> verifyUsernameAndPassword(String username, String password);

    Result<Boolean> registerUser(User user);
}
