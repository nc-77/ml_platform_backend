package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.Log;
import com.ml_platform_backend.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;

    public void insertLog(Log log) {
        logMapper.insert(log);
    }
}
