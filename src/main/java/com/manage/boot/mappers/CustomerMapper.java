package com.manage.boot.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manage.boot.pojo.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
