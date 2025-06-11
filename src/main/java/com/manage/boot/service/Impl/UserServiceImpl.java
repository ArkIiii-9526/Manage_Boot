package com.manage.boot.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manage.boot.mappers.UserMapper;
import com.manage.boot.pojo.User;
import com.manage.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Override
    public User selectUserByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public boolean isUserExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return userMapper.selectOne(queryWrapper) != null;
    }
}
