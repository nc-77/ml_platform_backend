package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.FileService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @GetMapping("/files/{id}/content")
    public ResponseEntity getFileContent(@PathVariable Integer id) throws FileNotFoundException {
        String json = fileService.getFileJsonContent(id);
        return new ResponseEntity(Code.SUCCESS.getValue(), json, Code.SUCCESS.getDescription());
    }

    @GetMapping("/files/download/{id}")
    public org.springframework.http.ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) throws IOException {
        File file = fileService.getFileById(id);
        byte[] data = Files.readAllBytes(Path.of(file.getFilePath()));

        // 解决中文编码
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new org.springframework.http.ResponseEntity<>(data, headers, org.springframework.http.HttpStatus.OK);
    }
}
