package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CommentMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.UserMapper;
import com.itheima.simpleShoppingMallDemo.Model.Comment;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.ModelVO.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台评论管理服务
 */
@Service
public class Admin_CommentService extends ServiceImpl<CommentMapper, Comment> {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 分页查询评论
     */
    public IPage<CommentVO> getCommentPage(Page<CommentVO> page, Long userId, Long productId) {
        // 先查询评论
        Page<Comment> commentPage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Comment::getUserId, userId);
        }
        if (productId != null) {
            wrapper.eq(Comment::getProductId, productId);
        }
        wrapper.orderByDesc(Comment::getCreatedAt);

        IPage<Comment> commentResult = this.page(commentPage, wrapper);

        // 转换为 VO 并填充商品名称和用户信息
        List<CommentVO> voList = commentResult.getRecords().stream().map(comment -> {
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(comment, vo);

            // 查询商品名称
            Product product = productMapper.selectById(comment.getProductId());
            if (product != null) {
                vo.setName(product.getName());
            }

            // 查询用户信息
            User user = userMapper.selectById(comment.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
                vo.setNickname(user.getNickname());
            }

            return vo;
        }).collect(Collectors.toList());

        // 构建返回结果
        Page<CommentVO> result = new Page<>(page.getCurrent(), page.getSize(), commentResult.getTotal());
        result.setRecords(voList);
        return result;
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