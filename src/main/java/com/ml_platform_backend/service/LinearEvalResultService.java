package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.LinearEvalResult;
import com.ml_platform_backend.mapper.LinearEvalResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinearEvalResultService {
    @Autowired
    private LinearEvalResultMapper linearEvalResultMapper;

    public int insert(LinearEvalResult linearEvalResult) {
        return linearEvalResultMapper.insert(linearEvalResult);
    }
}
