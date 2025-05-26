package com.itheima.simpleShoppingMallDemo.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.simpleShoppingMallDemo.Model.Order;
import com.itheima.simpleShoppingMallDemo.ModelDto.OrderDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {

    @Select("""
            SELECT
                o.order_id AS orderId,
                o.status,
                oi.product_id,
                p.name AS productName,
                p.product_url AS productImage,
                oi.price AS unitPrice,
                oi.quantity,
                (oi.price * oi.quantity) AS totalPrice
            FROM orders o
            JOIN order_items oi ON o.order_id = oi.order_id
            JOIN products p ON oi.product_id = p.product_id
            WHERE o.user_id = #{userId}
            """)
     List<OrderDto> selAllOrderByUserId(@Param("userId") Long userId);
    @Select("""
            SELECT
                o.order_id AS orderId,
                o.status,
                oi.product_id,
                p.name AS productName,
                p.product_url AS productImage,
                oi.price AS unitPrice,
                oi.quantity,
                (oi.price * oi.quantity) AS totalPrice
            FROM orders o
            JOIN order_items oi ON o.order_id = oi.order_id
            JOIN products p ON oi.product_id = p.product_id
            WHERE o.user_id = #{userId}
            AND o.status = 1
            """)
     List<OrderDto> selPaidOrderByUserId(@Param("userId")Long userId);
    @Select("""
            SELECT
                o.order_id AS orderId,
                o.status,
                oi.product_id,
                p.name AS productName,
                p.product_url AS productImage,
                oi.price AS unitPrice,
                oi.quantity,
                (oi.price * oi.quantity) AS totalPrice
            FROM orders o
            JOIN order_items oi ON o.order_id = oi.order_id
            JOIN products p ON oi.product_id = p.product_id
            WHERE o.user_id = #{userId}
            AND o.status = 0
            """)
    List<OrderDto> selUnpaidOrderByUserId(@Param("userId")Long userId);


}
