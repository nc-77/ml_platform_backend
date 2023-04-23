package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.LinearEvalResult;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.entry.PredictedFile;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.entry.vo.ModelTrainResp;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.LinearService;
import com.ml_platform_backend.service.ModelService;
import com.ml_platform_backend.service.PredictedFileService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinearController {
    @Autowired
    private LinearService linearService;
    @Autowired
    private FileService fileService;
    @Autowired
    private PredictedFileService pFileService;
    @Autowired
    private ModelService modelService;

    @Data
    static class TrainReq {
        private Integer fileId;
        private String classIndex;
        private Integer attributeSelectionMethod;
        private Double ridge;
    }

    @Data
    static class EvalReq {
        private Integer fileId;
    }

    @PostMapping("/train/linearRegression")
    public ResponseEntity linearRegressionTrain(@RequestBody TrainReq req) throws Exception {
        File dataSource = fileService.getFileById(req.fileId);
        LinearService.Options options = new LinearService.Options();
        options.setClassIndex(req.classIndex);
        options.setAttributeSelectionMethod(req.attributeSelectionMethod);
        options.setRidge(req.ridge);
        linearService.setOptions(options);
        Model model = linearService.train(dataSource);
        return new ResponseEntity(Code.SUCCESS.getValue(), new ModelTrainResp(model.getId(), model.getModelName()), Code.SUCCESS.getDescription());
    }

    @PostMapping("/eval/linearModel")
    public ResponseEntity evalModel(@RequestBody EvalReq req) throws Exception {
        PredictedFile pFile = pFileService.getPredictedFileById(req.fileId);
        Model model = modelService.getModelById(pFile.getModelId());
        File train = fileService.getFileById(model.getFileId());
        File test = fileService.getTestFile(train.getId());
        LinearEvalResult linearEvalResult = linearService.evalModel(model, train, test);
        return new ResponseEntity(Code.SUCCESS.getValue(), linearEvalResult, Code.SUCCESS.getDescription());
    }
}
