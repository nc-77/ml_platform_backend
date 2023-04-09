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
        // 如果fileName已经有后缀了，就替换掉
        String lastRandomString = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
        if (lastRandomString.length() == 8) {
            return fileName.replace(lastRandomString, randomString);
        }
        return fileName.replace(extension, "") + "_" + randomString + extension;
    }

}
