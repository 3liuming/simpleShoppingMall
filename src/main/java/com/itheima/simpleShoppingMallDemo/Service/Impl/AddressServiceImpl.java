package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.AddressMapper;
import com.itheima.simpleShoppingMallDemo.Model.Address;
import com.itheima.simpleShoppingMallDemo.Service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AddressService")
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
    @Override
    public boolean addAddress(Address address) {
        // 保存地址，createdAt 和 updatedAt 会自动填充
        return this.save(address);
    }

    @Override
    public boolean updateAddress(Address address) {
        // 需要验证地址是否属于当前用户
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getAddressId, address.getAddressId())
                .eq(Address::getUserId, address.getUserId());

        Address existingAddress = this.getOne(wrapper);
        if (existingAddress == null) {
            return false;
        }

        // 更新地址，updatedAt 会自动填充
        return this.updateById(address);
    }

    @Override
    public boolean deleteAddress(Long addressId, Long userId) {
        // 逻辑删除，只删除属于当前用户的地址
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getAddressId, addressId)
                .eq(Address::getUserId, userId);

        return this.remove(wrapper);
    }

    @Override
    public Address getAddressById(Long addressId, Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getAddressId, addressId)
                .eq(Address::getUserId, userId);

        return this.getOne(wrapper);
    }

    @Override
    public List<Address> listByUserId(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
                .orderByDesc(Address::getCreatedAt);

        return this.list(wrapper);
    }

}
