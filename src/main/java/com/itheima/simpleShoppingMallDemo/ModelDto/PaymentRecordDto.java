package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

// ==================== 支付记录DTO ====================
@Data
public class PaymentRecordDto {
    private Long paymentId;
    private BigDecimal amount;
    private Long status;
    private Timestamp createdAt;

    // 关联的订单信息
    private Long orderId;
    private BigDecimal orderTotalPrice;
    private Long orderStatus;
    private Timestamp orderCreatedAt;

    // 关联的订单项列表
    private List<OrderItemDto> orderItems;
}