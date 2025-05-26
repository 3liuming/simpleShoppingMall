package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.Category;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HomeService extends IService<Product> {

    Result<List<Category>> selCategories();
    Result<IPage<Product>> selProducts(Integer page, Integer perpage);
    Result<IPage<Product>> selProductsByCategoryId(Integer page, Integer perPage,Long categoryId);

    Result<Boolean> addCartWithPidAndUid(Long userId,Long productId);
}
