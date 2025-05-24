package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.CartProductDto;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Service.CartService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CartService")
public class CartServiceImpl extends ServiceImpl<ProductMapper, Product> implements CartService {

    @Autowired
    ProductMapper productMapper;
    @Override
    public Result<List<CartProductDto>> selectByUserId(Long userId){
        return Result.success(productMapper.selectByUserId(userId));
    }
}
