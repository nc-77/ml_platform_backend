package com.ml_platform_backend.entry.result;

public enum Code {
    UPLOAD_OK(20050, "数据源上传成功"),

    UPLOAD_ERR(20051,"数据源上传失败");

    private final int value;
    private final String description;

    Code(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static Code fromValue(int value) {
        for (Code code : values()) {
            if (code.value == value) {
                return code;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + value);
    }
}
