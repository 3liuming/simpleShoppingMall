package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CategoryMapper;
import com.itheima.simpleShoppingMallDemo.Model.Category;
import com.itheima.simpleShoppingMallDemo.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 后台商品类型管理服务
 */
@Service
@RequiredArgsConstructor
public class Admin_CategoryService extends ServiceImpl<CategoryMapper, Category> implements IService<Category> {

    private final CategoryMapper categoryMapper;

    /**
     * 添加商品类型
     */
    public Result<Category> addCategory(Category category, Long userId) {
        if (category == null) {
            return Result.fail("类型信息不能为空");
        }
        if (!StringUtils.hasText(category.getName())) {
            return Result.fail("类型名称不能为空");
        }

        // 检查类型名称是否已存在
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, category.getName());
        Long count = categoryMapper.selectCount(wrapper);
        if (count > 0) {
            return Result.fail("类型名称已存在");
        }

        boolean success = this.save(category);
        if (success) {
            return Result.success(category);
        }
        return Result.fail("添加类型失败");
    }

    /**
     * 删除商品类型（逻辑删除）
     */
    public Result<Void> deleteCategory(Long categoryId, Long userId) {
        if (categoryId == null) {
            return Result.fail("类型ID不能为空");
        }

        Category category = this.getById(categoryId);
        if (category == null) {
            return Result.fail("类型不存在");
        }

        // 可以添加检查：是否有商品使用该类型
        // 如果有商品使用，可以选择禁止删除或提示

        boolean success = this.removeById(categoryId);
        if (success) {
            return Result.success();
        }
        return Result.fail("删除类型失败");
    }

    /**
     * 更新商品类型信息
     */
    public Result<Category> updateCategory(Category category, Long userId) {
        if (category == null || category.getCategoryId() == null) {
            return Result.fail("类型信息不完整");
        }

        Category existCategory = this.getById(category.getCategoryId());
        if (existCategory == null) {
            return Result.fail("类型不存在");
        }

        // 如果修改了名称，检查新名称是否已存在
        if (StringUtils.hasText(category.getName()) &&
                !category.getName().equals(existCategory.getName())) {
            LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Category::getName, category.getName());
            wrapper.ne(Category::getCategoryId, category.getCategoryId());
            Long count = categoryMapper.selectCount(wrapper);
            if (count > 0) {
                return Result.fail("类型名称已存在");
            }
        }

        boolean success = this.updateById(category);
        if (success) {
            return Result.success(category);
        }
        return Result.fail("更新类型失败");
    }

    /**
     * 根据类型ID查询类型详情
     */
    public Result<Category> getCategoryById(Long categoryId) {
        if (categoryId == null) {
            return Result.fail("类型ID不能为空");
        }

        Category category = this.getById(categoryId);
        if (category == null) {
            return Result.fail("类型不存在");
        }

        return Result.success(category);
    }

    /**
     * 分页查询商品类型列表（支持类型名搜索）
     */
    public Result<Page<Category>> listCategories(Integer pageNum, Integer pageSize, String name) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(name)) {
            wrapper.like(Category::getName, name);
        }

        wrapper.orderByDesc(Category::getCreatedAt);

        Page<Category> categoryPage = this.page(page, wrapper);
        return Result.success(categoryPage);
    }

    /**
     * 查询所有商品类型（不分页）
     */
    public Result<List<Category>> getAllCategories(String name) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(name)) {
            wrapper.like(Category::getName, name);
        }

        wrapper.orderByDesc(Category::getCreatedAt);

        List<Category> categories = this.list(wrapper);
        return Result.success(categories);
    }
}