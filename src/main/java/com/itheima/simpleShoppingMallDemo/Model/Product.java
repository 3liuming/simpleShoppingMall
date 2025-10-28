package com.itheima.simpleShoppingMallDemo.Model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@TableName("products")
public class Product {
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String productUrl;
    private String address;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updatedAt;
    @TableLogic
    private Integer hidden;

}
