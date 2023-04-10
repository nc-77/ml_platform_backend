package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.entry.PredictedFile;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.ModelService;
import com.ml_platform_backend.service.PredictService;
import com.ml_platform_backend.service.PredictedFileService;
import com.opencsv.exceptions.CsvValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.lazy.IBk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class PredictController {
    @Autowired
    private ModelService modelService;
    @Autowired
    private FileService fileService;
    @Autowired
    private PredictService predictService;

    @Autowired
    private PredictedFileService predictedFileService;

    @Data
    private static class predictReq {
        private Integer modelId;
        private Integer fileId;
    }

    @Data
    @AllArgsConstructor
    private static class respData {
        private Integer fileId;
        private String fileName;
    }

    @PostMapping("/predict")
    public ResponseEntity predictLModel(@RequestBody predictReq req) throws Exception {
        Model model = modelService.getModelById(req.modelId);
        File testDataSet = fileService.getFileById(req.fileId);
        if (model.getModelClass().equals(LinearRegression.class.toString())) {
            PredictedFile labeledFile = predictService.predictLinearModel(model, testDataSet);
            return new ResponseEntity(Code.SUCCESS.getValue(), new respData(labeledFile.getId(), labeledFile.getFileName()), Code.SUCCESS.getDescription());
        } else if (model.getModelClass().equals(IBk.class.toString())) {
            PredictedFile labeledFile = predictService.predictKNNModel(model, testDataSet);
            return new ResponseEntity(Code.SUCCESS.getValue(), new respData(labeledFile.getId(), labeledFile.getFileName()), Code.SUCCESS.getDescription());
        }
        return new ResponseEntity(Code.FAILED.getValue(), null, Code.FAILED.getDescription());
    }

    @GetMapping("/predictedFiles/{id}/labelValues")
    public ResponseEntity getPredictedFileLabelValues(@PathVariable Integer id) {
        List<Object> labelValues = predictedFileService.getPredictedFileLabelValues(id);
        return new ResponseEntity(Code.SUCCESS.getValue(), labelValues, Code.SUCCESS.getDescription());
    }

    @GetMapping("/predictedFiles/{id}/content")
    public ResponseEntity getPredictedFilesContent(@PathVariable Integer id) {
        String json = predictedFileService.getFileJsonContent(id);
        return new ResponseEntity(Code.SUCCESS.getValue(), json, Code.SUCCESS.getDescription());
    }

    @GetMapping("/predictedFiles/{id}/fieldList")
    public ResponseEntity getPredictedFilesFieldList(@PathVariable Integer id) throws CsvValidationException, IOException {
        PredictedFile predictedFile = predictedFileService.getPredictedFileById(id);
        String[] line = fileService.getFirstLine(predictedFile.getOriginFileId());
        return new ResponseEntity(Code.SUCCESS.getValue(), line, Code.SUCCESS.getDescription());
    }

    @GetMapping("/predictedFiles/download/{id}")
    public org.springframework.http.ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) throws IOException {
        PredictedFile file = predictedFileService.getPredictedFileById(id);
        byte[] data = Files.readAllBytes(Path.of(file.getFilePath()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new org.springframework.http.ResponseEntity<>(data, headers, org.springframework.http.HttpStatus.OK);
    }


}
