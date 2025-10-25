package com.itheima.simpleShoppingMallDemo.Controller;


import com.itheima.simpleShoppingMallDemo.Model.Address;
import com.itheima.simpleShoppingMallDemo.Model.Shipment;
import com.itheima.simpleShoppingMallDemo.Service.AddressService;
import com.itheima.simpleShoppingMallDemo.Service.ShipmentService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private AddressService addressService;

    /**
     * 新增发货记录
     */
    @PostMapping
    public boolean add(@RequestBody Shipment shipment) {
        return shipmentService.save(shipment);
    }

    /**
     * 删除发货记录（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return shipmentService.removeById(id);
    }

    /**
     * 更新发货记录
     */
    @PutMapping
    public boolean update(@RequestBody Shipment shipment) {
        return shipmentService.updateById(shipment);
    }

    /**
     * 根据ID查询发货记录
     */
    @GetMapping("/{id}")
    public Shipment getById(@PathVariable Long id) {
        return shipmentService.getById(id);
    }

    /**
     * 查询所有发货记录
     */
    @GetMapping("/list")
    public List<Shipment> list() {
        return shipmentService.list();
    }

    /**
     * 根据用户ID查询发货记录
     */
    @GetMapping("/user/{userId}")
    public List<Shipment> getByUserId(@PathVariable Long userId) {
        return shipmentService.getByUserId(userId);
    }

    /**
     * 根据订单ID查询发货记录
     */
    @GetMapping("/order/{orderId}")
    public List<Shipment> getByOrderId(@PathVariable Long orderId) {
        return shipmentService.getByOrderId(orderId);
    }

    /**
     * 更新发货状态
     */
    @PutMapping("/{id}/status")
    public boolean updateStatus(@PathVariable Long id, @RequestParam Long status) {
        return shipmentService.updateShipmentStatus(id, status);
    }

    /**
     * 更新物流单号
     */
    @PutMapping("/{id}/tracking")
    public boolean updateTracking(@PathVariable Long id, @RequestParam String trackingNumber) {
        return shipmentService.updateTrackingInfo(id, trackingNumber);
    }

    /**
     * 更新物流单号
     */
    @GetMapping("/address/{addressId}")
    public Result<Address> getAddressByAddress(@PathVariable Long addressId,
                                      HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(addressService.getAddressById(addressId,userId));
    }
}