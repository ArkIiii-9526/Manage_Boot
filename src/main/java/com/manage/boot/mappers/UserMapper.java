package com.manage.boot.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manage.boot.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
