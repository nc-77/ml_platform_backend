package com.ml_platform_backend.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utils {
    public String getBaseName(String fileName) {
        return fileName.split("\\.")[0];
    }

    public String getSuffixName(String fileName) {
        return fileName.split("\\.")[1];
    }

    public String getFileRandomName(String fileName) {
        // 为fileName添加随机后缀
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String randomString = UUID.randomUUID().toString().substring(0, 8);
        return fileName.replace(extension, "") + "_" + randomString + extension;
    }

}
