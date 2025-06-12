package com.manage.boot.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manage.boot.mappers.RoomMapper;
import com.manage.boot.pojo.Room;
import com.manage.boot.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
    private final RoomMapper roomMapper;

    @Autowired
    public RoomServiceImpl(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }


    @Override
    public Page<Room> pageRooms(Page<Room> page, LambdaQueryWrapper<Room> wrapper) {
        return roomMapper.selectPage(page, wrapper);
    }

    @Override
    public int insertRoom(Room room) {
        return roomMapper.insert(room);
    }

    @Override
    public int updateRoom(Room room) {
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Room::getRoomId, room.getRoomId());
        return roomMapper.update(room, queryWrapper);
    }

    @Override
    public int deleteRoom(String id) {
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Room::getRoomId, id);
        return roomMapper.delete(queryWrapper);
    }

    @Override
    public int countRoom() {
        return 0;
    }

    @Override
    public boolean isRoomExist(String id) {
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Room::getRoomId, id);
        return roomMapper.selectOne(queryWrapper) != null;
    }
}
