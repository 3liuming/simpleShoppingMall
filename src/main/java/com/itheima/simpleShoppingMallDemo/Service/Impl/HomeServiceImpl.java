package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CartMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.CategoryMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.CartItem;
import com.itheima.simpleShoppingMallDemo.Model.Category;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Service.HomeService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("homeService")
public class HomeServiceImpl extends ServiceImpl<ProductMapper, Product> implements HomeService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CartMapper cartMapper;

    @Override
    public Result<List<Category>> selCategories(){

        Result<List<Category>> result = new Result<>();
        return result.success(categoryMapper.selectList(null));
    }

    @Override
    public Result<IPage<Product>> selProducts(Integer page, Integer perPage){
        Page<Product> productPage = new Page<>(page, perPage);
        return Result.success(productMapper.selectPage(productPage,null));
    }
    @Override
    public Result<IPage<Product>> selProductsByCategoryId(Integer page, Integer perPage,Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        Page<Product> productPage = new Page<>(page, perPage);
        wrapper.eq(Product::getCategoryId, categoryId);
        return Result.success(productMapper.selectPage(productPage,wrapper));
    }

    @Override
    public Result<Boolean> addCartWithPidAndUid(Long userId, Long productId) {
        // 查询该用户是否已将该商品加入购物车
        QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("product_id", productId);

        CartItem existingItem = cartMapper.selectOne(queryWrapper);

        if (existingItem != null) {
            // 已存在，则数量 +1
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            int updateRows = cartMapper.updateById(existingItem);
            if (updateRows > 0) {
                return Result.success(true);
            } else {
                return Result.fail("更新购物车数量失败");
            }
        } else {
            // 不存在，则插入新项
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(1);
            int insertRows = cartMapper.insert(cartItem);
            if (insertRows > 0) {
                return Result.success(true);
            } else {
                return Result.fail("新增购物车项失败");
            }
        }
    }

}
