package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.ShipmentMapper;
import com.itheima.simpleShoppingMallDemo.Model.Shipment;
import com.itheima.simpleShoppingMallDemo.Service.ShipmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("ShipmentService")
public class ShipmentServiceImpl extends ServiceImpl<ShipmentMapper, Shipment> implements ShipmentService{
    @Override
    public List<Shipment> getByUserId(Long userId) {
        LambdaQueryWrapper<Shipment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shipment::getUserId, userId)
                .orderByDesc(Shipment::getCreatedAt);
        return this.list(wrapper);
    }

    @Override
    public List<Shipment> getByOrderId(Long orderId) {
        LambdaQueryWrapper<Shipment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shipment::getOrderId, orderId)
                .orderByDesc(Shipment::getCreatedAt);
        return this.list(wrapper);
    }

    @Override
    public boolean updateShipmentStatus(Long shippingId, Long status) {
        LambdaUpdateWrapper<Shipment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Shipment::getShippingId, shippingId)
                .set(Shipment::getShipmentStatus, status);

        // 如果状态是已发货(1)，更新发货时间
        if (status == 1) {
            wrapper.set(Shipment::getShippedAt, LocalDateTime.now());
        }
        // 如果状态是已签收(2)，更新签收时间
        else if (status == 2) {
            wrapper.set(Shipment::getDeliveredAt, LocalDateTime.now());
        }

        return this.update(wrapper);
    }

    @Override
    public boolean updateTrackingInfo(Long shippingId, String trackingNumber) {
        LambdaUpdateWrapper<Shipment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Shipment::getShippingId, shippingId)
                .set(Shipment::getTrackingNumber, trackingNumber);
        return this.update(wrapper);
    }
}
