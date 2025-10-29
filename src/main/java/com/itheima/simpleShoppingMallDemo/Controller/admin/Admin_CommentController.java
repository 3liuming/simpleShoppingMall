package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.Comment;
import com.itheima.simpleShoppingMallDemo.ModelVO.CommentVO;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_CommentService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台评论管理控制器
 */
@RestController
@RequestMapping("/admin/comment")
public class Admin_CommentController {

    @Autowired
    private Admin_CommentService commentService;

    /**
     * 分页查询评论列表(关联用户信息)
     */
    @GetMapping("/page")
    public Result<IPage<CommentVO>> getCommentPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long productId) {
        Page<CommentVO> page = new Page<>(current, size);
        IPage<CommentVO> result = commentService.getCommentPage(page, userId, productId);
        return Result.success(result);
    }

    /**
     * 根据ID查询评论详情
     */
    @GetMapping("/{id}")
    public Result<Comment> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        return Result.success(comment);
    }

    /**
     * 更新评论
     */
    @PutMapping
    public Result<Comment> updateComment(@RequestBody Comment comment) {
        Comment result = commentService.updateComment(comment);
        return Result.success(result);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success();
    }
}