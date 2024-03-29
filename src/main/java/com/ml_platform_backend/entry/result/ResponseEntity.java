package com.ml_platform_backend.entry.result;

import lombok.Data;

@Data
public class ResponseEntity {
    private int code;
    private Object data;
    private String message;

    public ResponseEntity(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
