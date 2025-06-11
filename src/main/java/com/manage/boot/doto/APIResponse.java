package com.manage.boot.doto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // 添加双参数构造器（用于没有data的情况）
    public APIResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
}