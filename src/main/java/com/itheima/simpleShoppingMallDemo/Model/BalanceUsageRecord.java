package com.itheima.simpleShoppingMallDemo.Model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@TableName("balance_usage_record")
public class BalanceUsageRecord {
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;
    private Long paymentId;
    private Long userId;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private BigDecimal balanceUsed;
    private String transactionType;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updatedAt;
    @TableLogic
    private Integer hidden;
}
