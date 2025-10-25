package com.itheima.simpleShoppingMallDemo.ModelDto;

import lombok.Data;

@Data
public class CommentInputDto {

    private Long productId;

    private String content;

    // base64图片，可选
    private String commentImage;
}
