package com.ml_platform_backend.entry.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModelTrainResp {
    private Integer modelId;
    private String modelName;
}
