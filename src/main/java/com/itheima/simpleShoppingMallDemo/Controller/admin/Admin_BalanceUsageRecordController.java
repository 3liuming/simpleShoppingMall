package com.itheima.simpleShoppingMallDemo.Controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.BalanceUsageRecord;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_BalanceUsageRecordService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台余额记录管理控制器
 */
@RestController
@RequestMapping("/admin/balance-record")
public class Admin_BalanceUsageRecordController {

    @Autowired
    private Admin_BalanceUsageRecordService balanceRecordService;

    /**
     * 分页查询余额记录列表
     */
    @GetMapping("/page")
    public Result<IPage<BalanceUsageRecord>> getBalanceRecordPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId) {
        Page<BalanceUsageRecord> page = new Page<>(current, size);
        IPage<BalanceUsageRecord> result = balanceRecordService.getBalanceRecordPage(page, userId);
        return Result.success(result);
    }

    /**
     * 根据ID查询余额记录详情
     */
    @GetMapping("/{id}")
    public Result<BalanceUsageRecord> getBalanceRecordById(@PathVariable Long id) {
        BalanceUsageRecord record = balanceRecordService.getBalanceRecordById(id);
        return Result.success(record);
    }

    /**
     * 更新余额记录
     */
    @PutMapping
    public Result<BalanceUsageRecord> updateBalanceRecord(@RequestBody BalanceUsageRecord record) {
        BalanceUsageRecord result = balanceRecordService.updateBalanceRecord(record);
        return Result.success(result);
    }

    /**
     * 删除余额记录
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteBalanceRecord(@PathVariable Long id) {
        balanceRecordService.deleteBalanceRecord(id);
        return Result.success();
    }
}