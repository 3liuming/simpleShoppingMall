package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.Payment;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_PaymentService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台支付管理控制器
 */
@RestController
@RequestMapping("/admin/payment")
public class Admin_PaymentController {

    @Autowired
    private Admin_PaymentService paymentService;

    /**
     * 分页查询支付列表
     */
    @GetMapping("/page")
    public Result<IPage<Payment>> getPaymentPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status) {
        Page<Payment> page = new Page<>(current, size);
        IPage<Payment> result = paymentService.getPaymentPage(page, userId, status);
        return Result.success(result);
    }

    /**
     * 根据ID查询支付详情
     */
    @GetMapping("/{id}")
    public Result<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return Result.success(payment);
    }

    /**
     * 更新支付
     */
    @PutMapping
    public Result<Payment> updatePayment(@RequestBody Payment payment) {
        Payment result = paymentService.updatePayment(payment);
        return Result.success(result);
    }

    /**
     * 删除支付
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return Result.success();
    }
}
