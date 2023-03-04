package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.FileService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("/files/dataSets")
    public ResponseEntity getAllDataSets() {
        List<File> dataSets = fileService.getAllPredFiles();
        return new ResponseEntity(Code.SUCCESS.getValue(), dataSets, Code.SUCCESS.getDescription());
    }

    @GetMapping("/files/{id}/fieldList")
    public ResponseEntity getFileFieldList(@PathVariable Integer id) throws CsvValidationException, IOException {
        String[] fieldList = fileService.getFirstLine(id);
        return new ResponseEntity(Code.SUCCESS.getValue(), fieldList, Code.SUCCESS.getDescription());
    }
}
