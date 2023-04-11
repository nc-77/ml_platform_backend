package com.ml_platform_backend.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinearEvalResult extends BaseEntity {
    private double correlationCoefficient;
    private double meanAbsoluteError;
    private double rootMeanSquaredError;
    private double relativeAbsoluteError;
    private double rootRelativeSquaredError;
    private int numInstances;
}
