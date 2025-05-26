package com.itheima.simpleShoppingMallDemo.Interceptor;

import com.itheima.simpleShoppingMallDemo.common.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截所有 API 请求，从头里取出 My_token, 校验通过才放行，否则重定向到 login.html
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;

    public JwtInterceptor(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }

        String token = auth.substring(7);
        if (!jwtTokenUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }

        // 如果需要，可以把用户名放到 request attribute
        request.setAttribute("username", jwtTokenUtil.getUsernameFromToken(token));
        request.setAttribute("userId",jwtTokenUtil.getUserIdFromToken(token));
        return true;
    }
}
