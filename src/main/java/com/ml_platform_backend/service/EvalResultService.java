package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.EvalResult;
import com.ml_platform_backend.mapper.EvalResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvalResultService {
    @Autowired
    private EvalResultMapper evalResultMapper;

    public int insert(EvalResult evalResult) {
        return evalResultMapper.insert(evalResult);
    }
}
