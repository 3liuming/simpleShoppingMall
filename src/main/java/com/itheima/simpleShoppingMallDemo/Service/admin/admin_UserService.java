package com.itheima.simpleShoppingMallDemo.Service.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.simpleShoppingMallDemo.Mapper.UserMapper;
import com.itheima.simpleShoppingMallDemo.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("adminUserService")
public class admin_UserService {

    @Autowired
    private UserMapper mapper;

    public IPage<User> list(long page, long size) {
        return mapper.selectPage(new Page<>(page, size), null);
    }

    public User getById(Long id) {
        return mapper.selectById(id);
    }

    public boolean create(User entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(User entity) {
        return mapper.updateById(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }
}