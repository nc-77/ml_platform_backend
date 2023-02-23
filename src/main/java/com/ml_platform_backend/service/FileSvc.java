package com.ml_platform_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileSvc {
    @Autowired
    private FileMapper fileMapper;

    public File getFileById(Integer id) {
        return fileMapper.selectById(id);
    }

    public List<File> getAllPredFiles() {
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_pre", 1);
        return fileMapper.selectList(queryWrapper);
    }
}
