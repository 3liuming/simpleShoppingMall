package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CommentMapper;
import com.itheima.simpleShoppingMallDemo.Model.Comment;
import org.springframework.stereotype.Service;

/**
 * 后台评论管理服务
 */
@Service
public class Admin_CommentService extends ServiceImpl<CommentMapper, Comment> {

    /**
     * 分页查询评论
     */
    public IPage<Comment> getCommentPage(Page<Comment> page, Long userId, Long productId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Comment::getUserId, userId);
        }
        if (productId != null) {
            wrapper.eq(Comment::getProductId, productId);
        }
        wrapper.orderByDesc(Comment::getCreatedAt);
        return this.page(page, wrapper);
    }

    /**
     * 根据ID查询评论
     */
    public Comment getCommentById(Long id) {
        return this.getById(id);
    }

    /**
     * 更新评论
     */
    public Comment updateComment(Comment comment) {
        this.updateById(comment);
        return comment;
    }

    /**
     * 删除评论
     */
    public void deleteComment(Long id) {
        this.removeById(id);
    }
}