package com.itheima.simpleShoppingMallDemo.ModelVO;

import com.itheima.simpleShoppingMallDemo.Model.*;
import lombok.Data;

@Data
public class Admin_ShipmentVO extends Shipment {

    /**
     * 商品信息
     */
    private Product product;

    /**
     * 订单信息
     */
    private Order order;

    /**
     * 支付信息
     */
    private Payment payment;

    /**
     * 用户信息
     */
    private User user;

    /**
     * 地址信息
     */
    private Address address;

    /**
     * 支付状态文本: 未支付/已支付
     */
    private String paymentStatusText;

    /**
     * 发货状态文本: 待发货/已发货/已签收
     */
    private String shipmentStatusText;
}