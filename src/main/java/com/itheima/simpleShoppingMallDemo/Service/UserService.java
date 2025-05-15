package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<User> {

    boolean isUsernameExist(String username);

    boolean verifyUsernameAndPassword(String username, String password);

    boolean registerUser(User user);
}
