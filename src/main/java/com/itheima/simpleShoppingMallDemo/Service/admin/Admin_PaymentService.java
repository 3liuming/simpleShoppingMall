package com.itheima.simpleShoppingMallDemo.Service.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.PaymentMapper;
import com.itheima.simpleShoppingMallDemo.Model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 后台支付管理服务
 */
@Service
public class Admin_PaymentService extends ServiceImpl<PaymentMapper, Payment> {

    /**
     * 分页查询支付
     */
    public IPage<Payment> getPaymentPage(Page<Payment> page, Long userId, Integer status) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Payment::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(Payment::getStatus, status);
        }
        wrapper.orderByDesc(Payment::getCreatedAt);
        return this.page(page, wrapper);
    }

    /**
     * 根据ID查询支付
     */
    public Payment getPaymentById(Long id) {
        return this.getById(id);
    }

    /**
     * 更新支付
     */
    public Payment updatePayment(Payment payment) {
        this.updateById(payment);
        return payment;
    }

    /**
     * 删除支付
     */
    public void deletePayment(Long id) {
        this.removeById(id);
    }
}
