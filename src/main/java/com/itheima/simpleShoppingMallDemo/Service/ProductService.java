package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService extends IService<Product> {
    IPage<Product> selProducts(Integer page,Integer perpage);

    IPage<Product> selProductsByCategoryId(Integer page, Integer perPage,Long categoryId);
}
