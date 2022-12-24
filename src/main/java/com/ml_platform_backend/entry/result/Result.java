package com.ml_platform_backend.entry.result;

import lombok.Data;

@Data
public class Result {
    private Code code;
    private Object data;
    private String message;

    public Result(Code code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
