package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.PredictedFile;
import com.ml_platform_backend.mapper.PredictedFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictedFileService {
    @Autowired
    private PredictedFileMapper predictedFileMapper;

    public int insertPredictedFile(PredictedFile predictedFile) {
        return predictedFileMapper.insert(predictedFile);
    }

    public PredictedFile getPredictedFileById(Integer id) {
        return predictedFileMapper.selectById(id);
    }
}
