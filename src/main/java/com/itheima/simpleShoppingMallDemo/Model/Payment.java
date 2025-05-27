package com.itheima.simpleShoppingMallDemo.Model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@TableName("payments")
public class Payment {
    @TableId(value = "payment_id", type = IdType.AUTO)
    private Long paymentId;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private Long status;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updatedAt;
    @TableLogic
    private Integer hidden;
}
