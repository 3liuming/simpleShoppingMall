package com.itheima.simpleShoppingMallDemo.Service.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CategoryMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.Category;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.ModelVO.ProductVO;
import com.itheima.simpleShoppingMallDemo.common.Result;
import com.itheima.simpleShoppingMallDemo.common.UploadImage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台商品管理服务
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class Admin_ProductService extends ServiceImpl<ProductMapper, Product> implements IService<Product> {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    /**
     * 添加商品
     */
    public Result<Product> addProduct(Product product, Long userId) {
        if (product == null) {
            return Result.fail("商品信息不能为空");
        }
        if (!StringUtils.hasText(product.getName())) {
            return Result.fail("商品名称不能为空");
        }
        if (product.getPrice() == null || product.getPrice().doubleValue() < 0) {
            return Result.fail("商品价格不合法");
        }
        if (product.getCategoryId() == null) {
            return Result.fail("商品类型不能为空");
        }
        if (product.getProductUrl() == null){
            return Result.fail("商品图片不能为空");
        }
        //插入图片
        String imgUrl = UploadImage.uploadPostImage(product.getProductUrl(),"/product/");
        //验证图片是否插入成功
        if (imgUrl.startsWith("图片") || imgUrl.startsWith("保存失败")){
            throw new RuntimeException(imgUrl);
        }
        //将图片的相对路径存储到product中
        product.setProductUrl(imgUrl);

        // 验证类型是否存在
        Category category = categoryMapper.selectById(product.getCategoryId());
        if (category == null || category.getHidden() == 1) {
            return Result.fail("商品类型不存在或已删除");
        }

        boolean success = this.save(product);
        if (success) {
            return Result.success(product);
        }
        return Result.fail("添加商品失败");
    }

    /**
     * 删除商品（逻辑删除）
     */
    public Result<Void> deleteProduct(Long productId, Long userId) {
        if (productId == null) {
            return Result.fail("商品ID不能为空");
        }

        Product product = this.getById(productId);
        if (product == null) {
            return Result.fail("商品不存在");
        }

        boolean success = this.removeById(productId);
        if (success) {
            return Result.success();
        }
        return Result.fail("删除商品失败");
    }

    /**
     * 更新商品信息
     */
    public Result<Product> updateProduct(Product product, Long userId) {
        if (product == null || product.getProductId() == null) {
            return Result.fail("商品信息不完整");
        }

        Product existProduct = this.getById(product.getProductId());
        if (existProduct == null) {
            return Result.fail("商品不存在");
        }

        // 如果更新了类型，验证类型是否存在
        if (product.getCategoryId() != null) {
            Category category = categoryMapper.selectById(product.getCategoryId());
            if (category == null || category.getHidden() == 1) {
                return Result.fail("商品类型不存在或已删除");
            }
        }

        //判断不为空，而且传入了base64位字符串，且不为相对路径
        if (product.getProductUrl() != null && product.getProductUrl().length() > 50){
            String imgUrl = UploadImage.uploadPostImage(product.getProductUrl(),"/product/");
            //验证图片是否插入成功
            if (imgUrl.startsWith("图片") || imgUrl.startsWith("保存失败")){
                throw new RuntimeException(imgUrl);
            }
            //将图片的相对路径存储到product中
            product.setProductUrl(imgUrl);
        }
        //插入图片

        boolean success = this.updateById(product);
        if (success) {
            return Result.success(product);
        }
        return Result.fail("更新商品失败");
    }

    /**
     * 根据商品ID查询商品详情（含类型信息）
     */
    public Result<ProductVO> getProductById(Long productId) {
        if (productId == null) {
            return Result.fail("商品ID不能为空");
        }

        Product product = this.getById(productId);
        if (product == null) {
            return Result.fail("商品不存在");
        }

        ProductVO productVO = convertToVO(product);
        return Result.success(productVO);
    }

    /**
     * 分页查询商品列表（支持商品名搜索，含类型信息）
     */
    public Result<Page<ProductVO>> listProducts(Integer pageNum, Integer pageSize, String name) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(name)) {
            wrapper.like(Product::getName, name);
        }

        wrapper.orderByDesc(Product::getCreatedAt);

        Page<Product> productPage = this.page(page, wrapper);

        // 转换为VO
        Page<ProductVO> voPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        List<ProductVO> voList = productPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return Result.success(voPage);
    }

    /**
     * 查询所有商品（不分页，含类型信息）
     */
    public Result<List<ProductVO>> getAllProducts(String name) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(name)) {
            wrapper.like(Product::getName, name);
        }

        wrapper.orderByDesc(Product::getCreatedAt);

        List<Product> products = this.list(wrapper);
        List<ProductVO> voList = products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return Result.success(voList);
    }

    /**
     * 转换Product为ProductVO（包含类型信息）
     */
    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);

        // 查询类型信息
        if (product.getCategoryId() != null) {
            Category category = categoryMapper.selectById(product.getCategoryId());
            if (category != null && category.getHidden() == 0) {
                vo.setCategoryId(category.getCategoryId());
                vo.setCategoryName(category.getName());
            }
        }

        return vo;
    }
}

