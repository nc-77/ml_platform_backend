package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.LinearService;
import lombok.AllArgsConstructor;
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

    @Data
    static class LinearRegressionReq {
        private Integer fileId;
        private String classIndex;
        private String[] removeIndex;
    }

    @Data
    @AllArgsConstructor
    static class LinearRegressionResp {
        private Integer modelId;
        private String modelName;
    }

    @PostMapping("/train/linearRegression")
    public ResponseEntity LinearRegressionTrain(@RequestBody LinearRegressionReq req) throws Exception {
        File dataSource = fileService.getFileById(req.fileId);
        LinearService.Options options = new LinearService.Options(req.classIndex, req.removeIndex);
        linearService.setOptions(options);
        Model model = linearService.LinearRegressionTrain(dataSource);
        return new ResponseEntity(Code.SUCCESS.getValue(), new LinearRegressionResp(model.getId(), model.getModelName()), Code.SUCCESS.getDescription());
    }
}
