package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.simpleShoppingMallDemo.Model.CartItem;
import com.itheima.simpleShoppingMallDemo.Service.admin.admin_CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/cart_items")
public class AdminCartItemController {

    @Autowired
    private admin_CartItemService service;

    @GetMapping
    public IPage<CartItem> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.list(page, size);
    }

    @GetMapping("/{id}")
    public CartItem get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public boolean create(@RequestBody CartItem body) {
        return service.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody CartItem body) {
        return service.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }
}

