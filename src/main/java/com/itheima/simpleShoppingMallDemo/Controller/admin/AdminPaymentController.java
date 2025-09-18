package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.simpleShoppingMallDemo.Model.Payment;
import com.itheima.simpleShoppingMallDemo.Service.admin.admin_PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/payments")
public class AdminPaymentController {

    @Autowired
    private admin_PaymentService service;

    @GetMapping
    public IPage<Payment> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.list(page, size);
    }

    @GetMapping("/{id}")
    public Payment get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public boolean create(@RequestBody Payment body) {
        return service.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody Payment body) {
        return service.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
