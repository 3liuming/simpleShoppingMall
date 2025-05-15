package com.itheima.simpleShoppingMallDemo.a_Mytest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hello {
    @RequestMapping ("/hello")
    public String hello_test(){

        return "hello spring boot";
    }
}
