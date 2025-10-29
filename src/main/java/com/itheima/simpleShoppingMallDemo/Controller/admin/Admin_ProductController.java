package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.ModelVO.ProductVO;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_ProductService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 后台商品管理控制器
 */
@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class Admin_ProductController {

    private final Admin_ProductService productService;

    /**
     * 添加商品
     */
    @PostMapping("/add")
    public Result<Product> addProduct(@RequestBody Product product, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return productService.addProduct(product, userId);
    }

    /**
     * 删除商品（逻辑删除）
     */
    @DeleteMapping("/delete/{productId}")
    public Result<Void> deleteProduct(@PathVariable Long productId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return productService.deleteProduct(productId, userId);
    }

    /**
     * 更新商品信息
     */
    @PutMapping("/update")
    public Result<Product> updateProduct(@RequestBody Product product, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return productService.updateProduct(product, userId);
    }

    /**
     * 根据商品ID查询商品详情（含类型信息）
     */
    @GetMapping("/get/{productId}")
    public Result<ProductVO> getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    /**
     * 分页查询商品列表（支持商品名搜索，含类型信息）
     */
    @GetMapping("/list")
    public Result<Page<ProductVO>> listProducts(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        return productService.listProducts(pageNum, pageSize, name);
    }

    /**
     * 查询所有商品（不分页，含类型信息）
     */
    @GetMapping("/all")
    public Result<java.util.List<ProductVO>> getAllProducts(@RequestParam(required = false) String name) {
        return productService.getAllProducts(name);
    }
}
