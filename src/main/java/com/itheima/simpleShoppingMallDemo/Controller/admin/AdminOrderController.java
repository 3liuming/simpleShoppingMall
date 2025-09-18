package com.itheima.simpleShoppingMallDemo.Controller.admin;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.Service.admin.admin_OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private admin_OrderService service;

    @GetMapping
    public IPage<Order> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.list(page, size);
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public boolean create(@RequestBody Order body) {
        return service.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody Order body) {
        return service.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
