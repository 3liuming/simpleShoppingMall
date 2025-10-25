package com.itheima.simpleShoppingMallDemo.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.simpleShoppingMallDemo.Model.Shipment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShipmentMapper extends BaseMapper<Shipment> {
    /**
     * 根据用户ID查询发货记录
     */
    List<Shipment> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据订单ID查询发货记录
     */
    List<Shipment> selectByOrderId(@Param("orderId") Long orderId);
}
