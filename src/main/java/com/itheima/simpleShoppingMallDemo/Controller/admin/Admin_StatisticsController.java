package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.itheima.simpleShoppingMallDemo.ModelVO.StatisticsVO;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_StatisticsService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计数据Controller
 */
@RestController
@RequestMapping("/admin/statistics")
@RequiredArgsConstructor
public class Admin_StatisticsController {

    private final Admin_StatisticsService statisticsService;

    /**
     * 获取主页统计数据
     *
     * @return 统计数据
     */
    @GetMapping("/home")
    public Result<StatisticsVO> getHomeStatistics() {
        StatisticsVO statistics = statisticsService.getHomeStatistics();
        return Result.success(statistics);
    }
}
