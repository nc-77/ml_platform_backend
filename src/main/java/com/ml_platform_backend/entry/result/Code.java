package com.ml_platform_backend.entry.result;

public enum Code {
    SUCCESS(20000, "操作成功"),
    FAILED(20001, "操作失败"),
    UPLOAD_OK(20050, "数据源上传成功"),

    UPLOAD_ERR(20051, "数据源上传失败"),

    LOGIN_OK(20060, "登陆成功"),
    LOGIN_ERR(20061, "用户名或密码错误");
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
