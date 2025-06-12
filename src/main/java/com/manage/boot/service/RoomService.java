package com.manage.boot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manage.boot.pojo.Room;

public interface RoomService {
    Page<Room> pageRooms(Page<Room> page, LambdaQueryWrapper<Room> wrapper);

    int insertRoom(Room room);

    int updateRoom(Room room);

    int deleteRoom(String id);

    int countRoom();

    boolean isRoomExist(String id);
}
