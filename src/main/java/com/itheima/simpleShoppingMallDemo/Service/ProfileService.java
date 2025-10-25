package com.itheima.simpleShoppingMallDemo.Service;

import com.itheima.simpleShoppingMallDemo.Model.Address;
import com.itheima.simpleShoppingMallDemo.ModelDto.*;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface ProfileService {

    /**
     * 查询用户所有收货地址
     */
    Result<List<Address>> getUserAddresses(Long userId);
    /**
     * 添加收货地址
     */
    Result<Integer> addAddress(Address address);
    /**
     * 修改收货地址
     */
    Result<Integer> updateAddress(Address address);
    /**
     * 删除收货地址（逻辑删除）
     */
    Result<Integer> deleteAddress(Long addressId, Long userId);
    /**
     * 查询用户余额使用记录（关联支付和订单信息）
     */
    Result<List<BalanceUsageRecordDto>> getBalanceRecords(Long userId);
    /**
     * 删除余额记录（逻辑删除）
     */
    Result<Integer> deleteBalanceRecord(Long usageId, Long userId);
    /**
     * 查询用户收藏的商品
     */
    Result<List<FavoriteProductDto>> getFavoriteProducts(Long userId);
    /**
     * 删除商品收藏（逻辑删除）
     */
    Result<Integer> deleteFavoriteProduct(Long favoriteId, Long userId);
    /**
     * 查询用户的所有评论
     */
    Result<List<CommentDto>> getUserComments(Long userId);
    /**
     * 删除评论（逻辑删除）
     */
    Result<Integer> deleteComment(Long commentId, Long userId);
    /**
     * 查询用户的支付记录（关联订单和商品信息）
     */
    Result<List<PaymentRecordDto>> getPaymentRecords(Long userId);
    /**
     * 删除支付记录（逻辑删除）
     */
    Result<Integer> deletePaymentRecord(Long paymentId, Long userId);

    Result<UserDto> selUserByUsername(String username);

    Result<Integer> updateByUser(UserDto userDto);

    Result<Boolean> rechargeByuser(BigDecimal amount,Long userId);
}
