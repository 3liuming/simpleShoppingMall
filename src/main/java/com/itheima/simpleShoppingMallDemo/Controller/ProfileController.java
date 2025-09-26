package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.ModelDto.UserDto;
import com.itheima.simpleShoppingMallDemo.Service.ProfileService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @GetMapping("/userinfo")
    public Result<UserDto> selUserByUsername(HttpServletRequest request){
        System.out.println(request.getAttribute("userId"));
        System.out.println(request.getAttribute("username"));
        System.out.println(request.getAttribute("grade"));
        String username = (String)request.getAttribute("username");
        return profileService.selUserByUsername(username);
    }

    @PutMapping("/update")
    public Result<Integer> updateByUser(@RequestBody UserDto userDto){
        System.out.println("收到更新请求：" + userDto);
        return Result.success(profileService.updateByUser(userDto).getData());
    }

    @PatchMapping("/recharge")
    public Result<Boolean> rechargeByuser(@RequestBody Map<String, Object> data, HttpServletRequest request) {
        BigDecimal amount = new BigDecimal(data.get("amount").toString());
        BigDecimal lower = BigDecimal.ZERO;
        BigDecimal upper = new BigDecimal("100000");

        if (amount.compareTo(lower) < 0 || amount.compareTo(upper) > 0) {
            return Result.fail("充值金额必须在0到100,000之间");
        }

        try {
            return profileService.rechargeByuser(amount, (Long) request.getAttribute("userId"));
        } catch (Exception e) {
            return Result.fail("系统异常，请稍后重试");
        }
    }

}
