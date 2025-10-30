package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.ModelDto.CartBuyDto;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserAddressAndCartDto;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserAddressAndProductDto;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BuyService extends IService<Order> {

    Result<UserAddressAndProductDto> selUserProductByUidAndPid(Long userId, Long productId);

    Result<List<Long>> createOrderByUsernameAndQuantityAndPid(Long userId, CartBuyDto cartBuyDto);

    Result<Boolean> createPaymentByUsernameAndQuantityAndPid(Long userId, CartBuyDto cartBuyDto);

    /**
     * 获取购物车结算页面数据
     */
    Result<UserAddressAndCartDto> getCartListWithAddress(Long userId, String cartItemIds);
}
