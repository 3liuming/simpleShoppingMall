package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.CartMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.OrderItemMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.OrderMapper;
import com.itheima.simpleShoppingMallDemo.Mapper.ProductMapper;
import com.itheima.simpleShoppingMallDemo.Model.CartItem;
import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.ModelDto.CartNumRequest;
import com.itheima.simpleShoppingMallDemo.ModelDto.CartProductDto;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import com.itheima.simpleShoppingMallDemo.Service.BuyService;
import com.itheima.simpleShoppingMallDemo.Service.CartService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("CartService")
public class CartServiceImpl extends ServiceImpl<ProductMapper, Product> implements CartService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    BuyService buyService;

    @Override
    public Result<List<CartProductDto>> selectByUserId(Long userId){
        return Result.success(productMapper.selectByUserId(userId));
    }

    @Override
    public Result<Boolean> createPaymentByCartItemId(Long userId,List<Long> cartItemIds){
        List<Long> cartIds = cartItemIds.stream()
                .filter(cartItemId->cartItemId>0)
                .collect(Collectors.toList());

        for(long id : cartIds){
            CartItem cartItem = cartMapper.selectById(id);
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductId(cartItem.getProductId());
            boolean res = buyService.createPaymentByUsernameAndQuantityAndPid(userId,orderItem).getData();
            if (res){
                return Result.fail("编号为"+id+"的购物车结算失败");
            }
        }
        return Result.success();
    }

    @Override
    public Result<Boolean> updateCartWithQuantityByNum(CartNumRequest cartNumRequest){
        if (cartNumRequest != null && cartNumRequest.getQuantity() !=null && cartNumRequest.getQuantity() >0){
            Integer resc = cartMapper.update(new LambdaUpdateWrapper<CartItem>()
                .set(CartItem::getQuantity,cartNumRequest.getQuantity())
                .eq(CartItem::getCartItemId,cartNumRequest.getCartItemId()));
            if(resc <= 0){
                return Result.fail("购物车数量更新失败");
            }
        }else if (cartNumRequest != null && (cartNumRequest.getDelta() == 1 || cartNumRequest.getDelta() == -1)){
            CartItem cartItem = cartMapper.selectOne(new QueryWrapper<CartItem>()
                    .select("quantity")
                    .eq("cart_item_id", cartNumRequest.getCartItemId()));
            if(cartItem.getQuantity()>0){
                Integer quantity = cartItem.getQuantity()+cartNumRequest.getDelta();
                Integer resq = cartMapper.update(new LambdaUpdateWrapper<CartItem>()
                        .set(CartItem::getQuantity,quantity)
                        .eq(CartItem::getCartItemId,cartNumRequest.getCartItemId()));
                if (resq <= 0 ){
                    return Result.fail("购物车商品数量加减失败");
                }
            }
        }else {
            return Result.fail("无效的请求参数");
        }
        return Result.success(true);
    }
    @Override
    public Result<Boolean> deleteCartByCartItemId(Long userId,Long cartItemId){

        int resc = cartMapper.delete(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getCartItemId, cartItemId)
                .eq(CartItem::getUserId, userId)
        );

        if (resc <= 0){
            return Result.fail("删除失败");
        }
        return Result.success(true);
    }
}
