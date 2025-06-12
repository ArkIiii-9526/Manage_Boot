package com.manage.boot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manage.boot.pojo.Customer;



public interface CustomerService {
    Page<Customer> pageCustomers(Page<Customer> page, LambdaQueryWrapper<Customer> wrapper);

    int insertCustomer(Customer customer);

    int updateCustomer(Customer customer);

    int deleteCustomer(String idCard);

    long CountCustomer();

    Boolean isCustomerExist(String idCard);
}
