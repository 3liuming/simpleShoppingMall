package com.itheima.simpleShoppingMallDemo.ModelVO;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class CommentVO {

    private Long commentId;

    private Long userId;

    private String username;  // 用户名

    private String nickname;  // 昵称（优先显示）

    private Long productId;

    private String name;

    private String content;

    private String commentImageUrl;

    private Timestamp createdAt;
}
