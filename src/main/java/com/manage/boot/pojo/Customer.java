package com.manage.boot.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("CUSTOMER")
public class Customer {
    @TableField("NAME")
    private String name;
    @TableField("AGE")
    private Integer age;
    @TableField("SEX")
    private Integer sex;
    @TableField("PHONE")
    private String phone;
    @TableField("IDCARD")
    private String idCard;
}
