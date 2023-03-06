package com.ml_platform_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ml_platform_backend.entry.PredictedFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PredictedFileMapper extends BaseMapper<PredictedFile> {
}
