package com.itheima.simpleShoppingMallDemo.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.simpleShoppingMallDemo.Model.Comment;
import com.itheima.simpleShoppingMallDemo.ModelVO.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询商品的所有评论（关联用户信息）
     */
    @Select("SELECT c.comment_id, c.user_id, c.product_id, c.content, " +
            "c.comment_image_url, c.created_at, u.username, u.nickname " +
            "FROM comments c " +
            "LEFT JOIN users u ON c.user_id = u.user_id " +
            "WHERE c.product_id = #{productId} AND c.hidden = 0 " +
            "ORDER BY c.created_at DESC")
    List<CommentVO> selectCommentsByProductId(@Param("productId") Long productId);
}
