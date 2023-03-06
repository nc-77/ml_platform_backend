package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.entry.PredictedFile;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.ModelService;
import com.ml_platform_backend.service.PredictService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import weka.classifiers.functions.LinearRegression;

@RestController
public class PredictController {
    @Autowired
    private ModelService modelService;
    @Autowired
    private FileService fileService;
    @Autowired
    private PredictService predictService;

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
        }
        return new ResponseEntity(Code.FAILED.getValue(), null, Code.FAILED.getDescription());
    }
}
