package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Service.admin.admin_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private admin_UserService service;

    // 用户列表
    @GetMapping
    public IPage<User> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.list(page, size);
    }

    // 根据用户ID获取用户
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return service.getById(id);
    }

    // 用户名搜索功能
    @GetMapping("/search")
    public IPage<User> searchByUsername(
            @RequestParam String username,
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.searchByUsername(username, page, size);
    }

    // 添加用户
    @PostMapping
    public boolean create(@RequestBody User body) {
        return service.create(body);
    }

    // 修改用户信息
    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody User body) {
        // 更新特定用户的信息，通常会做一些字段的校验
        body.setUserId(id); // 保证更新的是正确的用户
        return service.update(body);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
