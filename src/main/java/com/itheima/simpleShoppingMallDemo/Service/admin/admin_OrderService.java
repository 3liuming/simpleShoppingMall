package com.itheima.simpleShoppingMallDemo.Service.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Mapper.OrderMapper;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminOrderService")
public class admin_OrderService {

    @Autowired
    private OrderMapper mapper;

    public IPage<Order> list(long page, long size) {
        return mapper.selectPage(new Page<>(page, size), null);
    }

    public Order getById(Long id) {
        return mapper.selectById(id);
    }

    public boolean create(Order entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(Order entity) {
        return mapper.updateById(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }
}
