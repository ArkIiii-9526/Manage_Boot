package com.manage.boot.dto;

import com.manage.boot.pojo.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPageResult {
    private List<Customer> customers;
    private long currentPage;
    private long totalPages;
    private long totalRecords;
}
