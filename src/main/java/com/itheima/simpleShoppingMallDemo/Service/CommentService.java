package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.Comment;
import com.itheima.simpleShoppingMallDemo.ModelDto.CommentInputDto;
import com.itheima.simpleShoppingMallDemo.ModelVO.CommentVO;

import java.util.List;

public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     */
    Comment addComment(Long userId, CommentInputDto commentInputDto);

    /**
     * 删除评论（仅本人可删除）
     */
    boolean deleteComment(Long commentId, Long userId);

    /**
     * 查询商品的所有评论
     */
    List<CommentVO> getCommentsByProductId(Long productId);

    boolean getCommentIdExitByUserIdAndPid(Long userId,Long pid);
}
