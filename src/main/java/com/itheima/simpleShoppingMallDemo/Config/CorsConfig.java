package com.itheima.simpleShoppingMallDemo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 配置跨域请求的规则
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://127.0.0.1:5500");  // 允许跨域的来源
        config.addAllowedHeader("*");  // 允许所有请求头
        config.addAllowedMethod("GET");  // 允许 GET 请求
        config.addAllowedMethod("POST"); // 允许 POST 请求
        config.addAllowedMethod("PUT");//允许 PUT 请求
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("OPTIONS");
        source.registerCorsConfiguration("/**", config);  // 全局生效

        return new CorsFilter(source);
    }
}
