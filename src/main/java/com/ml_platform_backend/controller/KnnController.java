package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.KnnService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KnnController {
    @Autowired
    private FileService fileService;
    @Autowired
    private KnnService knnService;

    @Data
    static class TrainReq {
        private Integer fileId;
        private Integer k;
    }

    @Data
    @AllArgsConstructor
    static class TrainResp {
        private Integer modelId;
        private String modelName;
    }

    @PostMapping("/train/knn")
    public ResponseEntity train(@RequestBody TrainReq req) throws Exception {
        File trainFile = fileService.getFileById(req.fileId);
        Model model = knnService.train(trainFile, req.k);
        return new ResponseEntity(Code.SUCCESS.getValue(), new TrainResp(model.getId(), model.getModelName()), Code.SUCCESS.getDescription());
    }
}
