package com.ml_platform_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ml_platform_backend.mapper")
public class MlPlatformBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MlPlatformBackendApplication.class, args);
    }

}
