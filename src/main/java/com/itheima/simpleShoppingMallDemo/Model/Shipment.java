package com.itheima.simpleShoppingMallDemo.Model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@TableName("shipments")
public class Shipment {
    @TableId(value = "shipment_id", type = IdType.AUTO)
    private Long shipmentId;
    private Long orderId;           // 订单ID
    private Long orderItemId;       // 订单明细ID
    private Long productId;         // 商品ID
    private Long userId;            //用户id
    private Integer quantity;       // 发货数量
    private Long addressId;         // 收货地址ID
    private Long paymentId;         // 支付ID（支付后补全）
    private Long shipmentStatus;    // 发货状态：0-待发货，1-已发货，2-已签收
    private String trackingNumber;  // 物流单号
    private Timestamp shippedAt;    // 发货时间
    private Timestamp deliveredAt;  // 签收时间
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Timestamp updatedAt;
    @TableField("hidden")
    private Integer hidden;
}