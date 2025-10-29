package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CommentMapper;
import com.itheima.simpleShoppingMallDemo.Model.Comment;
import com.itheima.simpleShoppingMallDemo.ModelDto.CommentInputDto;
import com.itheima.simpleShoppingMallDemo.ModelVO.CommentVO;
import com.itheima.simpleShoppingMallDemo.Service.CommentService;
import com.itheima.simpleShoppingMallDemo.common.UploadImage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("CommentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {
    @Resource
    private CommentMapper commentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment addComment(Long userId, CommentInputDto commentInputDto) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setProductId(commentInputDto.getProductId());
        comment.setContent(commentInputDto.getContent());

        // 如果有图片，保存图片
        if (commentInputDto.getCommentImage() != null &&
                !commentInputDto.getCommentImage().isEmpty()) {
            String imagePath = UploadImage.uploadPostImage(
                    commentInputDto.getCommentImage(),
                    "/comments/"
            );

            // 检查图片是否保存成功
            if (imagePath.startsWith("图片") || imagePath.startsWith("保存失败")) {
                throw new RuntimeException(imagePath);
            }

            comment.setCommentImageUrl(imagePath);
        }

        // 保存评论
        this.save(comment);
        return comment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComment(Long commentId, Long userId) {
        // 查询评论
        Comment comment = this.getById(commentId);

        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        // 验证是否是本人的评论
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除他人评论");
        }

        // 逻辑删除
        return this.removeById(commentId);
    }

    @Override
    public List<CommentVO> getCommentsByProductId(Long productId) {
        return commentMapper.selectCommentsByProductId(productId);
    }

    @Override
    public boolean getCommentIdExitByUserIdAndPid(Long userId, Long pid) {
        return commentMapper.selectCount(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getUserId, userId)
                        .eq(Comment::getProductId, pid)
                        .eq(Comment::getHidden,0)
        ) > 0;
    }
}
