package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.Address;
import com.itheima.simpleShoppingMallDemo.ModelDto.*;
import com.itheima.simpleShoppingMallDemo.Service.ProfileService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    // ==================== 用户信息相关 ====================

    /**
     * 获取当前用户信息
     */
    @GetMapping("/userinfo")
    public Result<UserDto> selUserByUsername(HttpServletRequest request){
        String username = (String)request.getAttribute("username");
        return profileService.selUserByUsername(username);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<Integer> updateByUser(@RequestBody UserDto userDto){
        System.out.println("收到更新请求：" + userDto);
        return profileService.updateByUser(userDto);
    }

    /**
     * 用户充值
     */
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

    // ==================== 地址管理 ====================

    /**
     * 查询当前用户所有收货地址
     */
    @GetMapping("/addresses")
    public Result<List<Address>> getUserAddresses(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.getUserAddresses(userId);
    }

    /**
     * 添加收货地址
     */
    @PostMapping("/addresses")
    public Result<Integer> addAddress(@RequestBody Address address, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        address.setUserId(userId);
        return profileService.addAddress(address);
    }

    /**
     * 修改收货地址
     */
    @PutMapping("/addresses/{addressId}")
    public Result<Integer> updateAddress(@PathVariable Long addressId,
                                         @RequestBody Address address,
                                         HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        address.setAddressId(addressId);
        address.setUserId(userId);
        return profileService.updateAddress(address);
    }

    /**
     * 删除收货地址（逻辑删除）
     */
    @DeleteMapping("/addresses/{addressId}")
    public Result<Integer> deleteAddress(@PathVariable Long addressId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.deleteAddress(addressId, userId);
    }

    // ==================== 余额记录管理 ====================

    /**
     * 查询当前用户余额使用记录
     */
    @GetMapping("/balance-records")
    public Result<List<BalanceUsageRecordDto>> getBalanceRecords(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.getBalanceRecords(userId);
    }

    /**
     * 删除余额记录（逻辑删除）
     */
    @DeleteMapping("/balance-records/{usageId}")
    public Result<Integer> deleteBalanceRecord(@PathVariable Long usageId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.deleteBalanceRecord(usageId, userId);
    }

    // ==================== 商品收藏管理 ====================

    /**
     * 查询当前用户收藏的商品
     */
    @GetMapping("/favorites")
    public Result<List<FavoriteProductDto>> getFavoriteProducts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.getFavoriteProducts(userId);
    }

    /**
     * 删除商品收藏（逻辑删除）
     */
    @DeleteMapping("/favorites/{favoriteId}")
    public Result<Integer> deleteFavoriteProduct(@PathVariable Long favoriteId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.deleteFavoriteProduct(favoriteId, userId);
    }

    // ==================== 评论管理 ====================

    /**
     * 查询当前用户的所有评论
     */
    @GetMapping("/comments")
    public Result<List<CommentDto>> getUserComments(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.getUserComments(userId);
    }

    /**
     * 删除评论（逻辑删除）
     */
    @DeleteMapping("/comments/{commentId}")
    public Result<Integer> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.deleteComment(commentId, userId);
    }

    // ==================== 支付记录管理 ====================

    /**
     * 查询当前用户的支付记录
     */
    @GetMapping("/payment-records")
    public Result<List<PaymentRecordDto>> getPaymentRecords(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.getPaymentRecords(userId);
    }

    /**
     * 删除支付记录（逻辑删除）
     */
    @DeleteMapping("/payment-records/{paymentId}")
    public Result<Integer> deletePaymentRecord(@PathVariable Long paymentId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return profileService.deletePaymentRecord(paymentId, userId);
    }
}