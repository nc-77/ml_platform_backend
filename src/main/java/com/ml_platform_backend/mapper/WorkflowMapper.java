package com.ml_platform_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ml_platform_backend.entry.Workflow;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkflowMapper extends BaseMapper<Workflow> {
}
