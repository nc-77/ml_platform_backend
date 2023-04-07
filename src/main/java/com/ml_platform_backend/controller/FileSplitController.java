package com.ml_platform_backend.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.CsvFileService;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileSplitController {
    private final FileService fileService;
    private final CsvFileService csvFileService;

    @Autowired
    private Utils utils;

    public FileSplitController(FileService fileService, CsvFileService csvFileService) {
        this.fileService = fileService;
        this.csvFileService = csvFileService;
    }

    @Data
    static class fileSplitReq {
        @JsonProperty(value = "fileId")
        private Integer originFileId;
        private String selectMethod;
        private double splitSize;
        private Integer randomSeed;

    }

    @Data
    @AllArgsConstructor
    class fileSplitResp {
        private Integer file1Id, file2Id;
        private String file1Name, file2Name;
    }

    @PostMapping("/files/split")
    public ResponseEntity fileSplit(@RequestBody fileSplitReq req) {
        File orginFile = fileService.getFileById(req.originFileId);

        List<String[]> fileData = null;
        try {
            fileData = csvFileService.readCsv(orginFile.getFilePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 划分数据集
        ImmutablePair<List<String[]>, List<String[]>> pair = csvFileService.splitDataset(fileData, req.splitSize, true, req.randomSeed);
        List<String[]> trainingSet = pair.getLeft();
        List<String[]> testSet = pair.getRight();

        // 写入划分文件
        File trainingSetFile = fileService.getNewFile(orginFile);
        File testSetFile = fileService.getNewFile(orginFile);
        fileService.insertFile(trainingSetFile);
        fileService.insertFile(testSetFile);

        try {
            csvFileService.writeCsv(trainingSet, trainingSetFile.getFilePath());
            csvFileService.writeCsv(testSet, testSetFile.getFilePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(Code.SUCCESS.getValue(), new fileSplitResp(trainingSetFile.getId(), testSetFile.getId(), trainingSetFile.getFileName(), testSetFile.getFileName()), Code.SUCCESS.getDescription());
    }
}
