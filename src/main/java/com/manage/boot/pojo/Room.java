package com.manage.boot.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ROOM")
public class Room {
    @TableField("ROOMID")
    private String roomId;
    @TableField("ROOMTYPE")
    private Integer roomType;
    @TableField("PRICE")
    private String price;
    @TableField("STATUS")
    private Integer status;
}
