package com.itheima.simpleShoppingMallDemo.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * 统计数据Mapper
 */
public interface StatisticsMapper extends BaseMapper<Object> {

    /**
     * 统计用户总数（不包括已删除的）
     */
    @Select("SELECT COUNT(*) FROM users WHERE hidden = 0")
    Long countUsers();

    /**
     * 统计商品总数（不包括已删除的）
     */
    @Select("SELECT COUNT(*) FROM products WHERE hidden = 0")
    Long countProducts();

    /**
     * 统计已完成订单数量（status=1且未删除）
     */
    @Select("SELECT COUNT(*) FROM orders WHERE status = 1 AND hidden = 0")
    Long countCompletedOrders();

    /**
     * 统计已发货商品总数量（shipment_status>=1且未删除）
     * 这里统计的是发货数量的总和
     */
    @Select("SELECT COALESCE(SUM(quantity), 0) FROM shipments WHERE shipment_status >= 1 AND hidden = 0")
    Long countShippedProducts();
}