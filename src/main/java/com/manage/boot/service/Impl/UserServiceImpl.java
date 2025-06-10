package com.manage.boot.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manage.boot.mappers.UserMapper;
import com.manage.boot.pojo.User;
import com.manage.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserMapper userMapper;
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Override
    public User selectUserByEmail(String email) {
        queryWrapper.eq("email",email);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public boolean isUserExist(String email) {
        queryWrapper.eq("email",email);
        return userMapper.selectOne(queryWrapper) != null;
    }
}
