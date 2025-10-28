package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.BalanceUsageRecordMapper;
import com.itheima.simpleShoppingMallDemo.Model.BalanceUsageRecord;
import org.springframework.stereotype.Service;

/**
 * 后台余额记录管理服务
 */
@Service
public class Admin_BalanceUsageRecordService extends ServiceImpl<BalanceUsageRecordMapper, BalanceUsageRecord> {

    /**
     * 分页查询余额记录
     */
    public IPage<BalanceUsageRecord> getBalanceRecordPage(Page<BalanceUsageRecord> page, Long userId) {
        LambdaQueryWrapper<BalanceUsageRecord> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(BalanceUsageRecord::getUserId, userId);
        }
        wrapper.orderByDesc(BalanceUsageRecord::getCreatedAt);
        return this.page(page, wrapper);
    }

    /**
     * 根据ID查询余额记录
     */
    public BalanceUsageRecord getBalanceRecordById(Long id) {
        return this.getById(id);
    }

    /**
     * 更新余额记录
     */
    public BalanceUsageRecord updateBalanceRecord(BalanceUsageRecord record) {
        this.updateById(record);
        return record;
    }

    /**
     * 删除余额记录
     */
    public void deleteBalanceRecord(Long id) {
        this.removeById(id);
    }
}