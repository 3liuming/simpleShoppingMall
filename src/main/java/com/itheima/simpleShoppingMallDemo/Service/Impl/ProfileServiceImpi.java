package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.UserMapper;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.Model.UserDto;
import com.itheima.simpleShoppingMallDemo.Service.ProfileService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("profileService")
public class ProfileServiceImpi extends ServiceImpl<UserMapper, User> implements ProfileService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Result<UserDto> selUserByUsername(String username){

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);

        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            return Result.fail("用户名查询结果为空");
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);
        return Result.success(userDto);
    }

    @Override
    public Result<Integer> updateByUser(UserDto userDto){

        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        int rows = userMapper.updateById(user);
        return Result.success(rows);
    }
}
