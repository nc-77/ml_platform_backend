package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.entry.vo.ModelTrainResp;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.KnnService;
import com.ml_platform_backend.service.ModelService;
import com.ml_platform_backend.service.PredictedFileService;
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
    @Autowired
    private PredictedFileService pFileService;
    @Autowired
    private ModelService modelService;

    @Data
    static class TrainReq {
        private Integer fileId;
        private Integer k;
    }

    @PostMapping("/train/knn")
    public ResponseEntity train(@RequestBody TrainReq req) throws Exception {
        File trainFile = fileService.getFileById(req.fileId);
        Model model = knnService.train(trainFile, req.k);
        return new ResponseEntity(Code.SUCCESS.getValue(), new ModelTrainResp(model.getId(), model.getModelName()), Code.SUCCESS.getDescription());
    }

}
