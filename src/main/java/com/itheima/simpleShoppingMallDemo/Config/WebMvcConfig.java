package com.itheima.simpleShoppingMallDemo.Config;

import com.itheima.simpleShoppingMallDemo.Interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 如果 login.html 在 static 目录下，需要让它可以被访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除登录页和登录接口，以及前端静态资源
                .excludePathPatterns(
                        "/login.html",           // 登录页面
                        "/login/**",              // 登录、注册 API
                        "/css/**", "/js/**",     // 静态目录
                        "/images/**",
                        "/favicon.ico",
                        "/home/catlist",
                        "/home/prolist",
                        "/home/bycategory",
                        "/product/getComments",
                        "/product/show"
                );
    }
}

