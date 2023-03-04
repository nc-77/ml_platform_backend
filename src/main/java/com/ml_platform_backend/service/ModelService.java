package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelService {
    @Autowired
    private ModelMapper modelMapper;

    public Integer insertModel(Model model) {
        return modelMapper.insert(model);
    }
}
