package com.ml_platform_backend.entry;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(autoResultMap = true)
public class ClassifyEvalResult extends BaseEntity {
    private Double pctCorrect;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Double> precisionRate;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Double> recall;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Double> fMeasure;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Integer> numInstances;
    private int totalNumInstances;
    private Integer modelId;
}
