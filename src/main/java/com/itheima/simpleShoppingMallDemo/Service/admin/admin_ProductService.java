package com.itheima.simpleShoppingMallDemo.Service.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminProductService")
public class admin_ProductService {

    @Autowired
    private ProductMapper mapper;

    public IPage<Product> list(long page, long size) {
        return mapper.selectPage(new Page<>(page, size), null);
    }

    public Product getById(Long id) {
        return mapper.selectById(id);
    }

    public boolean create(Product entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(Product entity) {
        return mapper.updateById(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }
}

