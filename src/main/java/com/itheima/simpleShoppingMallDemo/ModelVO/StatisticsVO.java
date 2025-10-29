package com.itheima.simpleShoppingMallDemo.ModelVO;

import lombok.Data;

/**
 * 主页统计数据VO
 */
@Data
public class StatisticsVO {
    /**
     * 用户总数
     */
    private Long userCount;

    /**
     * 商品总数
     */
    private Long productCount;

    /**
     * 已完成订单数量（已支付）
     */
    private Long completedOrderCount;

    /**
     * 已发货商品数量
     */
    private Long shippedProductCount;
}