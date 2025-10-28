package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.Shipment;
import com.itheima.simpleShoppingMallDemo.ModelDto.Admin_ShipmentVO;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_ShipmentService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台发货管理控制器
 */
@RestController
@RequestMapping("/admin/shipment")
public class Admin_ShipmentController {

    @Autowired
    private Admin_ShipmentService shipmentService;

    /**
     * 分页查询发货记录列表(包含商品、订单、支付信息)
     */
    @GetMapping("/page")
    public Result<IPage<Admin_ShipmentVO>> getShipmentPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Integer shipmentStatus,
            @RequestParam(required = false) Integer paymentStatus) {
        Page<Shipment> page = new Page<>(current, size);
        IPage<Admin_ShipmentVO> result = shipmentService.getShipmentPage(page, userId, orderId, shipmentStatus, paymentStatus);
        return Result.success(result);
    }

    /**
     * 根据ID查询发货记录详情(关联用户ID)
     */
    @GetMapping("/{id}")
    public Result<Admin_ShipmentVO> getShipmentById(@PathVariable Long id) {
        Admin_ShipmentVO shipment = shipmentService.getShipmentById(id);
        return Result.success(shipment);
    }

    /**
     * 更新发货记录
     */
    @PutMapping
    public Result<Shipment> updateShipment(@RequestBody Shipment shipment) {
        Shipment result = shipmentService.updateShipment(shipment);
        return Result.success(result);
    }

    /**
     * 删除发货记录
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
        return Result.success();
    }

    /**
     * 发货操作(传入物流单号，状态改为已发货)
     */
    @PostMapping("/ship/{id}")
    public Result<Shipment> shipOrder(
            @PathVariable Long id,
            @RequestParam String trackingNumber) {
        Shipment result = shipmentService.shipOrder(id, trackingNumber);
        return Result.success(result);
    }

    /**
     * 签收操作(状态改为已签收)
     */
    @PostMapping("/deliver/{id}")
    public Result<Shipment> deliverOrder(@PathVariable Long id) {
        Shipment result = shipmentService.deliverOrder(id);
        return Result.success(result);
    }
}