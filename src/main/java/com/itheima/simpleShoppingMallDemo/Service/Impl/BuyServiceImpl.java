package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.OrderMapper;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.Service.BuyService;
import org.springframework.stereotype.Service;

@Service("BuyService")
public class BuyServiceImpl extends ServiceImpl<OrderMapper, Order> implements BuyService {
}
