package com.manage.boot.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manage.boot.mappers.CustomerMapper;
import com.manage.boot.pojo.Customer;
import com.manage.boot.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper,Customer> implements CustomerService {

    private final CustomerMapper customerMapper;


    @Autowired
    public CustomerServiceImpl(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }


    @Override
    public Page<Customer> pageCustomers(Page<Customer> page, LambdaQueryWrapper<Customer> wrapper) {
        return customerMapper.selectPage(page, wrapper);
    }

    @Override
    public int insertCustomer(Customer customer) {
        return customerMapper.insert(customer);
    }

    @Override
    public int updateCustomer(Customer customer) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getIdCard,customer.getIdCard());
        return customerMapper.update(customer,queryWrapper);
    }

    @Override
    public int deleteCustomer(String idCard) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getIdCard,idCard);
        return customerMapper.delete(queryWrapper);
    }

    @Override
    public long CountCustomer() {
        return customerMapper.selectCount(null);
    }

    @Override
    public Boolean isCustomerExist(String idCard) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getIdCard,idCard);
        return customerMapper.selectOne(queryWrapper) != null;
    }
}
