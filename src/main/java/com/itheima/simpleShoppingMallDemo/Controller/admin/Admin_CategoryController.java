package com.itheima.simpleShoppingMallDemo.Controller.admin;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.Category;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_CategoryService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 后台商品类型管理控制器
 */
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class Admin_CategoryController {

    private final Admin_CategoryService categoryService;

    /**
     * 添加商品类型
     */
    @PostMapping("/add")
    public Result<Category> addCategory(@RequestBody Category category, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return categoryService.addCategory(category, userId);
    }

    /**
     * 删除商品类型（逻辑删除）
     */
    @DeleteMapping("/delete/{categoryId}")
    public Result<Void> deleteCategory(@PathVariable Long categoryId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return categoryService.deleteCategory(categoryId, userId);
    }

    /**
     * 更新商品类型信息
     */
    @PutMapping("/update")
    public Result<Category> updateCategory(@RequestBody Category category, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return categoryService.updateCategory(category, userId);
    }

    /**
     * 根据类型ID查询类型详情
     */
    @GetMapping("/get/{categoryId}")
    public Result<Category> getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    /**
     * 分页查询商品类型列表（支持类型名搜索）
     */
    @GetMapping("/list")
    public Result<Page<Category>> listCategories(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        return categoryService.listCategories(pageNum, pageSize, name);
    }

    /**
     * 查询所有商品类型（不分页）
     */
    @GetMapping("/all")
    public Result<java.util.List<Category>> getAllCategories(@RequestParam(required = false) String name) {
        return categoryService.getAllCategories(name);
    }
}