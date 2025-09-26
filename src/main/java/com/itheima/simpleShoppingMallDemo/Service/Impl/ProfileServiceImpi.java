package com.itheima.simpleShoppingMallDemo.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.simpleShoppingMallDemo.Mapper.UserMapper;
import com.itheima.simpleShoppingMallDemo.Model.User;
import com.itheima.simpleShoppingMallDemo.ModelDto.UserDto;
import com.itheima.simpleShoppingMallDemo.Service.ProfileService;
import com.itheima.simpleShoppingMallDemo.common.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

    @Override
    @Transactional
    public Result<Boolean> rechargeByuser(BigDecimal amount,Long userId){
        User user = userMapper.selectById(userId);
        BigDecimal endBalance = user.getBalance().add(amount);
        // 更新用户余额
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper
                .set(User::getBalance, endBalance)
                .eq(User::getUserId, userId);
        int balanceUpdateResult = userMapper.update(updateWrapper);

        if (balanceUpdateResult <= 0) {
            throw new RuntimeException("用户余额更新失败");
        }
        return Result.success(true);
    }
}
