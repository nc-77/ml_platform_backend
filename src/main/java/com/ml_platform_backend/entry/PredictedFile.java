package com.ml_platform_backend.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictedFile extends BaseEntity {
    private String fileName;
    private String filePath;
    private Integer modelId;
    private Integer originFileId;
}