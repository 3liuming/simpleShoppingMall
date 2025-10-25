package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CommentMapper;
import com.itheima.simpleShoppingMallDemo.Model.Comment;
import com.itheima.simpleShoppingMallDemo.ModelDto.CommentDTO;
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
    public Comment addComment(Long userId, CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setProductId(commentDTO.getProductId());
        comment.setContent(commentDTO.getContent());

        // 如果有图片，保存图片
        if (commentDTO.getCommentImage() != null &&
                !commentDTO.getCommentImage().isEmpty()) {
            String imagePath = UploadImage.uploadPostImage(
                    commentDTO.getCommentImage(),
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
}
