package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

// ==================== 评论DTO ====================
@Data
public class CommentDto {
    private Long commentId;
    private String content;
    private String commentImageUrl;
    private Timestamp createdAt;

    // 关联的商品信息
    private Long productId;
    private String productName;
    private String productUrl;
}

