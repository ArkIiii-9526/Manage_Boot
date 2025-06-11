package com.manage.boot.doto;

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
    private int currentPage;
    private int totalPages;
    private long totalRecords;
}
