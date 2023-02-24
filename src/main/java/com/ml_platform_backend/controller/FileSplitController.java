package com.ml_platform_backend.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.CsvFileSvc;
import com.ml_platform_backend.service.FileSvc;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class FileSplitController {
    private final FileSvc fileSvc;
    private final CsvFileSvc csvFileSvc;

    public FileSplitController(FileSvc fileSvc, CsvFileSvc csvFileSvc) {
        this.fileSvc = fileSvc;
        this.csvFileSvc = csvFileSvc;
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
        File orginFile = fileSvc.getFileById(req.originFileId);
        // 生成划分文件名
        String orginFileName = orginFile.getFileName();
        String baseName = orginFileName.split("\\.")[0];
        String suffixName = orginFileName.split(("\\."))[1];
        String trainingSetName = baseName + "-0." + suffixName;
        String testSetName = baseName + "-1." + suffixName;

        List<String[]> fileData = null;
        try {
            fileData = csvFileSvc.readCsv(orginFile.getFilePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 划分数据集
        ImmutablePair<List<String[]>, List<String[]>> pair = csvFileSvc.splitDataset(fileData, req.splitSize, true, req.randomSeed);
        List<String[]> trainingSet = pair.getLeft();
        List<String[]> testSet = pair.getRight();

        // 生成划分文件路径
        String currentDirectory = System.getProperty("user.dir");
        Path trainingSetPath = Paths.get(currentDirectory + "/dataSources/" + trainingSetName);
        Path testSetPath = Paths.get(currentDirectory + "/dataSources/" + testSetName);

        // 写入划分文件
        File trainingSetFile = new File(trainingSetName, trainingSetPath.toString());
        File testSetFile = new File(testSetName, testSetPath.toString());
        fileSvc.insertFile(trainingSetFile);
        fileSvc.insertFile(testSetFile);

        try {
            csvFileSvc.writeCsv(trainingSet, trainingSetPath.toString());
            csvFileSvc.writeCsv(testSet, testSetPath.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(Code.SUCCESS.getValue(), new fileSplitResp(trainingSetFile.getId(), testSetFile.getId(), trainingSetFile.getFileName(), testSetFile.getFileName()), Code.SUCCESS.getDescription());
    }
}
