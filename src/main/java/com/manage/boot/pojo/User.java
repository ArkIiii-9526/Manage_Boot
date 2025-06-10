package com.manage.boot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("S_USER")
public class User {
    @TableId(value = "EMAIL", type = IdType.AUTO)
    private String email;

    @TableField("NAME")
    private String name;

    @TableField("PASSWORD")
    private String password;
}
