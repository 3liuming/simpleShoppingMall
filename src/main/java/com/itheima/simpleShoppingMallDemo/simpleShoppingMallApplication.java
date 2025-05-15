package com.itheima.simpleShoppingMallDemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.itheima.simpleShoppingMallDemo.Mapper")
@SpringBootApplication
public class simpleShoppingMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(simpleShoppingMallApplication.class,args);
    }
}
