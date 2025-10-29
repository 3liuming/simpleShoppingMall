package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.UserFavoriteProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Model.UserFavoriteProduct;
import com.itheima.simpleShoppingMallDemo.ModelVO.UserFavoriteProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台用户收藏管理服务
 */
@Service
public class Admin_UserFavoriteProductService extends ServiceImpl<UserFavoriteProductMapper, UserFavoriteProduct> {

    /**
     * 分页查询收藏
     */
    @Autowired
    private ProductMapper productMapper;

    public IPage<UserFavoriteProductVO> getFavoritePage(Page<UserFavoriteProductVO> page, Long userId, Long productId) {
        // 先查询收藏列表
        Page<UserFavoriteProduct> favoritePage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<UserFavoriteProduct> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(UserFavoriteProduct::getUserId, userId);
        }
        if (productId != null) {
            wrapper.eq(UserFavoriteProduct::getProductId, productId);
        }
        wrapper.orderByDesc(UserFavoriteProduct::getCreatedAt);

        IPage<UserFavoriteProduct> favoriteResult = this.page(favoritePage, wrapper);

        // 转换为 VO 并填充商品名称
        List<UserFavoriteProductVO> voList = favoriteResult.getRecords().stream().map(favorite -> {
            UserFavoriteProductVO vo = new UserFavoriteProductVO();
            BeanUtils.copyProperties(favorite, vo);

            // 查询商品名称
            Product product = productMapper.selectById(favorite.getProductId());
            if (product != null) {
                vo.setName(product.getName());
            }

            return vo;
        }).collect(Collectors.toList());

        // 构建返回结果
        Page<UserFavoriteProductVO> result = new Page<>(page.getCurrent(), page.getSize(), favoriteResult.getTotal());
        result.setRecords(voList);
        return result;
    }

    /**
     * 根据ID查询收藏
     */
    public UserFavoriteProduct getFavoriteById(Long id) {
        return this.getById(id);
    }

    /**
     * 删除收藏
     */
    public void deleteFavorite(Long id) {
        this.removeById(id);
    }
}