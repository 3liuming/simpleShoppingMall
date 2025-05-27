package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserProductDto;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;

@Service
public interface BuyService extends IService<Order> {

    Result<UserProductDto> selUserProductByUidAndPid(Long userId, Long productId);

    Result<Boolean> createOrderByUsernameAndQuantityAndPid(Long userId, OrderItem orderItem);

}
