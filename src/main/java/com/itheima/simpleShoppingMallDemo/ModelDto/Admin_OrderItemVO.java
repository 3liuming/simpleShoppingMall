package com.itheima.simpleShoppingMallDemo.ModelDto;

import com.itheima.simpleShoppingMallDemo.Model.OrderItem;
import com.itheima.simpleShoppingMallDemo.Model.Product;
import lombok.Data;

/**
 * 后台订单项视图对象(包含商品信息)
 */
@Data
public class Admin_OrderItemVO extends OrderItem {

    /**
     * 商品信息
     */
    private Product product;
}
