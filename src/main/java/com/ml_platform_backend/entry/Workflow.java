package com.ml_platform_backend.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Workflow extends BaseEntity {
    private String workflowName;
    private String workflowDesc;
    private Integer userId;
}
