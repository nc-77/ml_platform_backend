package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.ClassifyEvalResult;
import com.ml_platform_backend.mapper.ClassifyEvalResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassifyEvalResultService {
    @Autowired
    private ClassifyEvalResultMapper classifyEvalResultMapper;

    public int insert(ClassifyEvalResult classifyEvalResult) {
        return classifyEvalResultMapper.insert(classifyEvalResult);
    }
}
