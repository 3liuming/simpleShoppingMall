package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;

@Service
public interface ProductService extends IService<Product> {
    Result<Product> selProductByProductId(Long productId);
}
