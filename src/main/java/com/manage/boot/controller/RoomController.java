package com.manage.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manage.boot.dto.APIResponse;
import com.manage.boot.dto.RoomPageResult;
import com.manage.boot.pojo.Room;
import com.manage.boot.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {
    private RoomService roomService;
    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(value = "/api/rooms")
    public APIResponse<RoomPageResult> getRooms(
            @RequestParam(value = "searchId", defaultValue = "") String searchId,
            @RequestParam(value = "currentPage", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        try {
            Page<Room> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
            if(!searchId.isEmpty()){
                wrapper.like(Room::getRoomId, searchId);
            }
            Page<Room> resultPage = roomService.pageRooms(page, wrapper);

            RoomPageResult pageResult = new RoomPageResult(
                    resultPage.getRecords(),
                    (int) resultPage.getCurrent(),
                    (int) resultPage.getPages(),
                    resultPage.getTotal()
            );
            return new APIResponse<>(true, "查询成功", pageResult);

        }catch (Exception e){
            return new APIResponse<>(false, "服务器错误: " + e.getMessage());
        }
    }
    @PutMapping("/api/editRooms/{roomId}")
    public APIResponse<Void> updateRoom(@PathVariable("roomId") String roomId, @RequestBody Room room) {
        try {
            if (!roomService.isRoomExist(roomId)) {
                return new APIResponse<>(false, "房间不存在");
            }
            roomService.updateRoom(room);
            return new APIResponse<>(true, "房间信息更新成功");
        } catch (Exception e) {
            return new APIResponse<>(false, "服务器错误: " + e.getMessage());
        }
    }
    @PostMapping("/api/addRooms")
    public APIResponse<Void> addRoom(@RequestBody Room room) {
        try {
            String roomId = room.getRoomId();
            if(roomService.isRoomExist(roomId)){
                return new APIResponse<>(false, "房间已存在");
            }
            roomService.insertRoom(room);
            return new APIResponse<>(true, "房间添加成功");
        }catch (Exception e){
            return new APIResponse<>(false, "服务器错误: " + e.getMessage());
        }
    }
    @DeleteMapping("/api/deleteRooms/{roomId}")
    public APIResponse<Void> deleteRoom(@PathVariable("roomId") String roomId) {
        try {
            if (!roomService.isRoomExist(roomId)) {
                return new APIResponse<>(false, "房间不存在");
            }
            roomService.deleteRoom(roomId);
            return new APIResponse<>(true, "房间删除成功");
        }catch (Exception e){
            return new APIResponse<>(false, "服务器错误: " + e.getMessage());
        }
    }
}
