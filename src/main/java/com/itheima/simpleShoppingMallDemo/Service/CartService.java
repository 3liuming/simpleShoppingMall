package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.CartProductDto;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService extends IService<Product> {

    Result<List<CartProductDto>> selectByUserId(Long userId);
}
