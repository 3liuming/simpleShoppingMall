package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Service.ProductService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ProductService")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    ProductMapper productMapper;

    public Result<Product> selProductByProductId(Long productId){
        Product product = productMapper.selectById(productId);
        return Result.success(product);
    }
}
