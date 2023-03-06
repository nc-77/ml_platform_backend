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

import java.nio.file.Path;
import java.nio.file.Paths;
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
        // 生成划分文件名
        String originFileName = orginFile.getFileName();
        String baseName = utils.getBaseName(originFileName);
        String suffixName = utils.getSuffixName(originFileName);
        String trainingSetName = baseName + "-0." + suffixName;
        String testSetName = baseName + "-1." + suffixName;

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

        // 生成划分文件路径
        String currentDirectory = System.getProperty("user.dir");
        Path trainingSetPath = Paths.get(currentDirectory + "/dataSources/" + trainingSetName);
        Path testSetPath = Paths.get(currentDirectory + "/dataSources/" + testSetName);

        // 写入划分文件
        File trainingSetFile = new File(trainingSetName, trainingSetPath.toString(), orginFile.getId());
        File testSetFile = new File(testSetName, testSetPath.toString(), orginFile.getId());
        fileService.insertFile(trainingSetFile);
        fileService.insertFile(testSetFile);

        try {
            csvFileService.writeCsv(trainingSet, trainingSetPath.toString());
            csvFileService.writeCsv(testSet, testSetPath.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(Code.SUCCESS.getValue(), new fileSplitResp(trainingSetFile.getId(), testSetFile.getId(), trainingSetFile.getFileName(), testSetFile.getFileName()), Code.SUCCESS.getDescription());
    }
}
