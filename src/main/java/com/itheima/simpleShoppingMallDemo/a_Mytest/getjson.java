package com.itheima.simpleShoppingMallDemo.a_Mytest;

import com.itheima.simpleShoppingMallDemo.Model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class getjson {

    @GetMapping("/getjson")
    public User getjson(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setUsername("张三");
        user.setPassword("123456");
        user.setUserId(1L);
        user.setEmail("123@qq.com");
        user.setPhone("123456789");
        user.setAddress("长安");
        user.setCreatedAt(timestamp);
        user.setUpdatedAt(timestamp);
        return user;
    }
}
