package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.KnnEvalResult;
import com.ml_platform_backend.mapper.KnnEvalResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KnnEvalResultService {
    @Autowired
    private KnnEvalResultMapper knnEvalResultMapper;

    public int insert(KnnEvalResult knnEvalResult) {
        return knnEvalResultMapper.insert(knnEvalResult);
    }
}
