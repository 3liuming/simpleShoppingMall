package com.itheima.simpleShoppingMallDemo.ModelDto;

import com.itheima.simpleShoppingMallDemo.Model.Order;
import lombok.Data;

import java.util.List;

/**
 * 后台订单视图对象(包含订单项和商品信息)
 */
@Data
public class Admin_OrderVO extends Order {

    /**
     * 订单项列表(包含商品信息)
     */
    private List<Admin_OrderItemVO> orderItems;
}