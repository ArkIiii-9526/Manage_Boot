package com.manage.boot.dto;

import com.manage.boot.pojo.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPageResult {
    private List<Room> rooms;
    private long currentPage;
    private long totalPages;
    private long totalRecords;
}
