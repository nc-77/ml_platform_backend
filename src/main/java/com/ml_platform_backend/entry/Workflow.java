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
public class Workflow extends BaseEntity {
    private String workflowName;
    private String workflowDesc;
    private Integer userId;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> graphJson;
}
