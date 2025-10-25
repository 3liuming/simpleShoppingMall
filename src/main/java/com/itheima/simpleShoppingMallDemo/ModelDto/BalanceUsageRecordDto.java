package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

// ==================== 余额记录DTO ====================
@Data
public class BalanceUsageRecordDto {
    private Long usageId;
    private Long paymentId;
    private Long userId;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private BigDecimal balanceUsed;
    private String transactionType;
    private Timestamp createdAt;

    // 关联的支付信息
    private BigDecimal paymentAmount;
    private Long paymentStatus;

    // 关联的订单信息
    private Long orderId;
    private BigDecimal orderTotalPrice;
    private Long orderStatus;

    // 关联的商品列表
    private List<ProductDto> products;
}