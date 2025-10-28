package com.itheima.simpleShoppingMallDemo.Controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Service.admin.Admin_UserService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台用户管理控制器
 */
@RestController
@RequestMapping("/admin/user")
public class Admin_UserController {

    @Autowired
    private Admin_UserService userService;

    /**
     * 分页查询用户列表(支持用户名或昵称搜索)
     */
    @GetMapping("/page")
    public Result<IPage<User>> getUserPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<User> page = new Page<>(current, size);
        IPage<User> result = userService.getUserPage(page, keyword);
        return Result.success(result);
    }

    /**
     * 根据ID查询用户详情
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<User> addUser(@RequestBody User user) {
        User result = userService.addUser(user);
        return Result.success(result);
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Result<User> updateUser(@RequestBody User user) {
        User result = userService.updateUser(user);
        return Result.success(result);
    }

    /**
     * 删除用户(级联删除关联数据)
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}