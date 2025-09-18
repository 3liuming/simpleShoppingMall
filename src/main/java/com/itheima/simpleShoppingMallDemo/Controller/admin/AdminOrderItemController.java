package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.Service.admin.admin_OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order_items")
public class AdminOrderItemController {

    @Autowired
    private admin_OrderItemService service;

    @GetMapping
    public IPage<OrderItem> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.list(page, size);
    }

    @GetMapping("/{id}")
    public OrderItem get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public boolean create(@RequestBody OrderItem body) {
        return service.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody OrderItem body) {
        return service.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
