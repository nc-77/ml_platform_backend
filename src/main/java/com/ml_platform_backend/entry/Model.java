package com.ml_platform_backend.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model extends BaseEntity {
    private String modelName;
    private String modelPath;
    private String modelClass;
    private Integer fileId;// 由fileId训练而来
}
