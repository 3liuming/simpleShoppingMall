package com.itheima.simpleShoppingMallDemo.Model;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@TableName("users")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    private String nickname;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private BigDecimal balance;
    private Integer grade;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updatedAt;
    @TableLogic
    private Integer hidden;
}
