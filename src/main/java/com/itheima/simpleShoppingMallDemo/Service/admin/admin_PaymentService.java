package com.itheima.simpleShoppingMallDemo.Service.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Mapper.PaymentMapper;
import com.itheima.simpleShoppingMallDemo.Model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminPaymentService")
public class admin_PaymentService {

    @Autowired
    private PaymentMapper mapper;

    public IPage<Payment> list(long page, long size) {
        return mapper.selectPage(new Page<>(page, size), null);
    }

    public Payment getById(Long id) {
        return mapper.selectById(id);
    }

    public boolean create(Payment entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(Payment entity) {
        return mapper.updateById(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }
}
