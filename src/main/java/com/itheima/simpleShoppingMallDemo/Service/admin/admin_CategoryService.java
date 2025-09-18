package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Mapper.CategoryMapper;
import com.itheima.simpleShoppingMallDemo.Model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("admin_CategoryService")
public class admin_CategoryService {

    @Autowired
    private CategoryMapper mapper;

    public IPage<Category> list(long page, long size) {
        return mapper.selectPage(new Page<>(page, size), null);
    }

    public Category getById(Long id) {
        return mapper.selectById(id);
    }

    public boolean create(Category entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(Category entity) {
        return mapper.updateById(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }
}