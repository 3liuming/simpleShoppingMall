package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductService")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Override
    public IPage<Product> selProducts(Integer page, Integer perPage){
        Page<Product> productPage = new Page<>(page, perPage);
        return productMapper.selectPage(productPage,null);
    }
    @Override
    public IPage<Product> selProductsByCategoryId(Integer page, Integer perPage,Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        Page<Product> productPage = new Page<>(page, perPage);
        wrapper.eq(Product::getCategoryId, categoryId);
        return productMapper.selectPage(productPage,wrapper);
    }
}
