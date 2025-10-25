package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.Address;
import com.itheima.simpleShoppingMallDemo.Model.Shipment;

import java.util.List;

public interface ShipmentService extends IService<Shipment> {
    /**
     * 根据用户ID查询发货记录
     */
    List<Shipment> getByUserId(Long userId);

    /**
     * 根据订单ID查询发货记录
     */
    List<Shipment> getByOrderId(Long orderId);

    /**
     * 更新发货状态
     */
    boolean updateShipmentStatus(Long shippingId, Long status);

    /**
     * 更新物流信息
     */
    boolean updateTrackingInfo(Long shippingId, String trackingNumber);
}
