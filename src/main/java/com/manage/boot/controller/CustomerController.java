package com.manage.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manage.boot.doto.APIResponse;
import com.manage.boot.doto.CustomerPageResult;
import com.manage.boot.pojo.Customer;
import com.manage.boot.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/api/customers")
    public APIResponse<CustomerPageResult> getCustomers(
            @RequestParam(value = "searchName", defaultValue = "") String searchName,
            @RequestParam(value = "currentPage", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        try {
            Page<Customer> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();

            // 添加查询条件（如果存在搜索词）
            if (!searchName.isEmpty()) {
                wrapper.like(Customer::getName, searchName);
            }

            // 使用Service层进行分页查询
            Page<Customer> resultPage = customerService.pageCustomers(page, wrapper);

            // 构建返回结果
            CustomerPageResult pageResult = new CustomerPageResult(
                    resultPage.getRecords(),
                    (int) resultPage.getCurrent(),
                    (int) resultPage.getPages(),
                    resultPage.getTotal()
            );

            return new APIResponse<>(true, "查询成功", pageResult);
        } catch (Exception e) {
            return new APIResponse<>(false, "服务器错误: " + e.getMessage());
        }
    }

    @PutMapping("/api/editCustomers/{idCard}")
    public APIResponse<Void> updateCustomer(@PathVariable("idCard") String idCard,
                                            @RequestBody Customer customer) {
        try {
            if (!customerService.isCustomerExist(idCard)) {
                return new APIResponse<>(false, "客户不存在");
            }

            customerService.updateCustomer(customer);
            return new APIResponse<>(true, "客户信息更新成功");
        } catch (Exception e) {
            return new APIResponse<>(false, "服务器错误: " + e.getMessage());
        }
    }

    @PostMapping("/api/addCustomers")
    public APIResponse<Void> addCustomer(@RequestBody Customer customer) {
        try {
            String idCard = customer.getIdCard();
            if (customerService.isCustomerExist(idCard)) {
                return new APIResponse<>(false, "客户已存在");
            }

            customerService.insertCustomer(customer);
            return new APIResponse<>(true, "客户添加成功");
        } catch (Exception e) {
            return new APIResponse<>(false, "服务器错误: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/deleteCustomers/{idCard}")
    public APIResponse<Void> deleteCustomer(@PathVariable("idCard") String idCard) {
        try {
            if (!customerService.isCustomerExist(idCard)) {
                return new APIResponse<>(false, "客户不存在");
            }

            customerService.deleteCustomer(idCard);
            return new APIResponse<>(true, "客户删除成功");
        } catch (Exception e) {
            return new APIResponse<>(false, "服务器错误: " + e.getMessage());
        }
    }
}