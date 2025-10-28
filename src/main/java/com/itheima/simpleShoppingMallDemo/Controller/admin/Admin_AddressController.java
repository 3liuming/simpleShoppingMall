package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.Address;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_AddressService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台地址管理控制器
 */
@RestController
@RequestMapping("/admin/address")
public class Admin_AddressController {

    @Autowired
    private Admin_AddressService addressService;

    /**
     * 分页查询地址列表
     */
    @GetMapping("/page")
    public Result<IPage<Address>> getAddressPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId) {
        Page<Address> page = new Page<>(current, size);
        IPage<Address> result = addressService.getAddressPage(page, userId);
        return Result.success(result);
    }

    /**
     * 根据ID查询地址详情
     */
    @GetMapping("/{id}")
    public Result<Address> getAddressById(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        return Result.success(address);
    }

    /**
     * 新增地址
     */
    @PostMapping
    public Result<Address> addAddress(@RequestBody Address address) {
        Address result = addressService.addAddress(address);
        return Result.success(result);
    }

    /**
     * 更新地址
     */
    @PutMapping
    public Result<Address> updateAddress(@RequestBody Address address) {
        Address result = addressService.updateAddress(address);
        return Result.success(result);
    }

    /**
     * 删除地址
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return Result.success();
    }
}