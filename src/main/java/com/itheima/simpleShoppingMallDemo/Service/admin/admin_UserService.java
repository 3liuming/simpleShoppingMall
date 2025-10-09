package com.itheima.simpleShoppingMallDemo.Service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class admin_UserService {

    @Autowired
    private UserMapper userMapper;

    // 获取用户列表（分页）
    public IPage<User> list(long page, long size) {
        IPage<User> userPage = new Page<>(page, size);
        return userMapper.selectPage(userPage, null); // 不加条件，直接分页查询所有
    }

    // 根据ID获取用户
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    // 根据用户名模糊搜索用户
    public IPage<User> searchByUsername(String username, long page, long size) {
        IPage<User> userPage = new Page<>(page, size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", username); // 模糊查询
        return userMapper.selectPage(userPage, wrapper);
    }

    // 创建新用户
    public boolean create(User user) {
        return userMapper.insert(user) > 0;
    }

    // 更新用户信息
    public boolean update(User user) {
        return userMapper.updateById(user) > 0;
    }

    // 删除用户
    public boolean delete(Long id) {
        return userMapper.deleteById(id) > 0;
    }
}
