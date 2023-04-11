package com.ml_platform_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ml_platform_backend.entry.KnnEvalResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KnnEvalResultMapper extends BaseMapper<KnnEvalResult> {
}
