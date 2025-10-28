package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.AddressMapper;
import com.itheima.simpleShoppingMallDemo.Model.Address;
import org.springframework.stereotype.Service;

/**
 * 后台地址管理服务
 */
@Service
public class Admin_AddressService extends ServiceImpl<AddressMapper, Address> {

    /**
     * 分页查询地址
     */
    public IPage<Address> getAddressPage(Page<Address> page, Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Address::getUserId, userId);
        }
        wrapper.orderByDesc(Address::getCreatedAt);
        return this.page(page, wrapper);
    }

    /**
     * 根据ID查询地址
     */
    public Address getAddressById(Long id) {
        return this.getById(id);
    }

    /**
     * 新增地址
     */
    public Address addAddress(Address address) {
        this.save(address);
        return address;
    }

    /**
     * 更新地址
     */
    public Address updateAddress(Address address) {
        this.updateById(address);
        return address;
    }

    /**
     * 删除地址
     */
    public void deleteAddress(Long id) {
        this.removeById(id);
    }
}