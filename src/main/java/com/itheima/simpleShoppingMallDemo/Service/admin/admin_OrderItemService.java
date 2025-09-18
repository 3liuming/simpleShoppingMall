package com.itheima.simpleShoppingMallDemo.Service.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Mapper.OrderItemMapper;
import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminOrderItemService")
public class admin_OrderItemService {

    @Autowired
    private OrderItemMapper mapper;

    public IPage<OrderItem> list(long page, long size) {
        return mapper.selectPage(new Page<>(page, size), null);
    }

    public OrderItem getById(Long id) {
        return mapper.selectById(id);
    }

    public boolean create(OrderItem entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(OrderItem entity) {
        return mapper.updateById(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }
}
