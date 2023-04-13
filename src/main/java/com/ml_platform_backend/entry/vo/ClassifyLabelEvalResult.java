package com.ml_platform_backend.entry.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassifyLabelEvalResult {
    private String labelName;
    private Double precisionRate;
    private Double recall;
    private Double fMeasure;
    private int numInstances;

    public ClassifyLabelEvalResult(String labelName) {
        this.labelName = labelName;
    }
}
