package com.ml_platform_backend.utils;

import org.springframework.stereotype.Component;

@Component
public class Utils {
    public String getBaseName(String fileName) {
        return fileName.split("\\.")[0];
    }

    public String getSuffixName(String fileName) {
        return fileName.split("\\.")[1];
    }
}
