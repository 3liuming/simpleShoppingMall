package com.itheima.simpleShoppingMallDemo.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.simpleShoppingMallDemo.Model.Address;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService extends IService<Address> {
    /**
     * 添加地址
     */
    boolean addAddress(Address address);

    /**
     * 更新地址
     */
    boolean updateAddress(Address address);

    /**
     * 删除地址（逻辑删除）
     */
    boolean deleteAddress(Long addressId, Long userId);

    /**
     * 根据ID查询地址
     */
    Address getAddressById(Long addressId, Long userId);

    /**
     * 查询用户所有地址
     */
    List<Address> listByUserId(Long userId);
}
