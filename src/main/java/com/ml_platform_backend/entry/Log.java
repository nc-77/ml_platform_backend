package com.ml_platform_backend.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log extends BaseEntity {
    private String httpMethod;
    private String httpPath;
    private Integer httpCode;
    private Long timeTaken;
    private String ip;
}
