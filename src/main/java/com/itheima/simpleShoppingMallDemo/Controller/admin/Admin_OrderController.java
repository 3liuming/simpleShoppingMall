package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.ModelDto.Admin_OrderVO;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_OrderService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台订单管理控制器
 */
@RestController
@RequestMapping("/admin/order")
public class Admin_OrderController {

    @Autowired
    private Admin_OrderService orderService;

    /**
     * 分页查询订单列表(包含订单项和商品信息)
     */
    @GetMapping("/page")
    public Result<IPage<Admin_OrderVO>> getOrderPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status) {
        Page<Order> page = new Page<>(current, size);
        IPage<Admin_OrderVO> result = orderService.getOrderPage(page, userId, status);
        return Result.success(result);
    }

    /**
     * 根据ID查询订单详情
     */
    @GetMapping("/{id}")
    public Result<Admin_OrderVO> getOrderById(@PathVariable Long id) {
        Admin_OrderVO order = orderService.getOrderById(id);
        return Result.success(order);
    }

    /**
     * 新增订单
     */
    @PostMapping
    public Result<Order> addOrder(@RequestBody Order order) {
        Order result = orderService.addOrder(order);
        return Result.success(result);
    }

    /**
     * 更新订单
     */
    @PutMapping
    public Result<Order> updateOrder(@RequestBody Order order) {
        Order result = orderService.updateOrder(order);
        return Result.success(result);
    }

    /**
     * 删除订单(级联删除订单项)
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return Result.success();
    }
}