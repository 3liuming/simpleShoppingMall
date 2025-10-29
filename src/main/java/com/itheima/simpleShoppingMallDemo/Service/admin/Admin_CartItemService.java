package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CartItemMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.CartItem;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.ModelVO.CartItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台购物车管理服务
 */
@Service
public class Admin_CartItemService extends ServiceImpl<CartItemMapper, CartItem> {

    @Autowired
    ProductMapper productMapper;
    /**
     * 分页查询购物车
     */

    public IPage<CartItemVO> getCartItemPage(Page<CartItemVO> page, Long userId, Long productId) {
        // 先查询购物车
        Page<CartItem> cartPage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(CartItem::getUserId, userId);
        }
        if (productId != null) {
            wrapper.eq(CartItem::getProductId, productId);
        }
        wrapper.orderByDesc(CartItem::getCreatedAt);

        IPage<CartItem> cartResult = this.page(cartPage, wrapper);

        // 转换为 VO 并填充商品名称
        List<CartItemVO> voList = cartResult.getRecords().stream().map(item -> {
            CartItemVO vo = new CartItemVO();
            BeanUtils.copyProperties(item, vo);

            // 查询商品名称
            Product product = productMapper.selectById(item.getProductId());
            if (product != null) {
                vo.setName(product.getName());
            }

            return vo;
        }).collect(Collectors.toList());

        // 构建返回结果
        Page<CartItemVO> result = new Page<>(page.getCurrent(), page.getSize(), cartResult.getTotal());
        result.setRecords(voList);
        return result;
    }

    /**
     * 根据ID查询购物车项
     */
    public CartItem getCartItemById(Long id) {
        return this.getById(id);
    }

    /**
     * 新增购物车项
     */
    public CartItem addCartItem(CartItem cartItem) {
        this.save(cartItem);
        return cartItem;
    }

    /**
     * 更新购物车项
     */
    public CartItem updateCartItem(CartItem cartItem) {
        this.updateById(cartItem);
        return cartItem;
    }

    /**
     * 删除购物车项
     */
    public void deleteCartItem(Long id) {
        this.removeById(id);
    }
}