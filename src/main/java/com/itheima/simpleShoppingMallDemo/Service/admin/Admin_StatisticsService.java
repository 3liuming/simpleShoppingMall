package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.itheima.simpleShoppingMallDemo.Mapper.StatisticsMapper;
import com.itheima.simpleShoppingMallDemo.ModelVO.StatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 统计数据Service
 */
@Service
@RequiredArgsConstructor
public class Admin_StatisticsService {

    private final StatisticsMapper statisticsMapper;

    /**
     * 获取主页统计数据
     */
    public StatisticsVO getHomeStatistics() {
        StatisticsVO vo = new StatisticsVO();

        // 统计用户数量
        vo.setUserCount(statisticsMapper.countUsers());

        // 统计商品数量
        vo.setProductCount(statisticsMapper.countProducts());

        // 统计已完成订单数量
        vo.setCompletedOrderCount(statisticsMapper.countCompletedOrders());

        // 统计已发货商品数量
        vo.setShippedProductCount(statisticsMapper.countShippedProducts());

        return vo;
    }
}