package com.itheima.simpleShoppingMallDemo.Controller;

import com.itheima.simpleShoppingMallDemo.Model.Comment;
import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.ModelDto.CommentInputDto;
import com.itheima.simpleShoppingMallDemo.ModelVO.CommentVO;
import com.itheima.simpleShoppingMallDemo.Service.BuyService;
import com.itheima.simpleShoppingMallDemo.Service.CommentService;
import com.itheima.simpleShoppingMallDemo.Service.ProductService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    BuyService buyService;
    @Resource
    private CommentService commentService;

    @GetMapping("/show")
    public Result<Product> selProductByProductId(@RequestParam("productId") Long productId){
        return productService.selProductByProductId(productId);
    }

    /**
     * 添加评论
     * POST /api/comments
     */
    @PostMapping
    public Map<String, Object> addComment(
            @RequestBody CommentInputDto commentInputDto,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 从request中获取userId（假设已经通过拦截器存入）
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "用户未登录");
                return result;
            }

            Comment comment = commentService.addComment(userId, commentInputDto);

            result.put("code", 200);
            result.put("message", "评论成功");
            result.put("data", comment);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "评论失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 删除评论
     * DELETE /api/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public Map<String, Object> deleteComment(
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 从request中获取userId
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "用户未登录");
                return result;
            }

            boolean success = commentService.deleteComment(commentId, userId);

            if (success) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 500);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 查询商品的所有评论
     * GET /api/comments/product/{productId}
     */
    @GetMapping("/getComments/{productId}")
    public Map<String, Object> getCommentsByProductId(@PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<CommentVO> comments = commentService.getCommentsByProductId(productId);

            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", comments);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }

        return result;
    }

    //以下接口已废弃
    @PostMapping("/create")
    public Result<Boolean> createOrderByUsernameAndQuantityAndPid(@RequestBody OrderItem orderItem,
                                                                  HttpServletRequest request){
//        return buyService.createOrderByUsernameAndQuantityAndPid((Long)request.getAttribute("userId"),orderItem);
        return Result.success();
    }

    @PostMapping("/nowbuy")
    public Result<Boolean> CreatePaymentByUserNameAndQuantityAndPid(@RequestBody OrderItem orderItem,
                                                                    HttpServletRequest request){
//        return buyService.createPaymentByUsernameAndQuantityAndPid((Long)request.getAttribute("userId"),orderItem);
        return Result.success();
    }
}
