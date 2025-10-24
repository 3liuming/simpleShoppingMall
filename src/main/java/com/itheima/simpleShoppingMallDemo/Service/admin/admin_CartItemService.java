package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Mapper.CartItemMapper;
import com.itheima.simpleShoppingMallDemo.Model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminCartItemService")
public class admin_CartItemService {

    @Autowired
    private CartItemMapper mapper;

    public IPage<CartItem> list(long page, long size) {
        return mapper.selectPage(new Page<>(page, size), null);
    }

    public CartItem getById(Long id) {
        return mapper.selectById(id);
    }

    public boolean create(CartItem entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(CartItem entity) {
        return mapper.updateById(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }
}
