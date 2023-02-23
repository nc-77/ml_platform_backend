package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.FileSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileController {
    @Autowired
    private FileSvc fileSvc;

    @GetMapping("/files/dataSets")
    public ResponseEntity getAllDataSets() {
        List<File> dataSets = fileSvc.getAllPredFiles();
        return new ResponseEntity(Code.SUCCESS.getValue(), dataSets, Code.SUCCESS.getDescription());
    }
}
