package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.ClassifyEvalResult;
import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.entry.PredictedFile;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.entry.vo.ClassifyEvalResultResp;
import com.ml_platform_backend.service.ClassifyService;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.ModelService;
import com.ml_platform_backend.service.PredictedFileService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassifyController {
    @Autowired
    private PredictedFileService pFileService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ClassifyService classifyService;

    @Data
    static class EvalReq {
        private Integer fileId;
    }

    @PostMapping("/eval/classify")
    public ResponseEntity eval(@RequestBody EvalReq req) throws Exception {
        PredictedFile pFile = pFileService.getPredictedFileById(req.fileId);
        Model model = modelService.getModelById(pFile.getModelId());
        File train = fileService.getFileById(model.getFileId());
        File test = fileService.getTestFile(train.getId());
        ClassifyEvalResult evalResult = classifyService.eval(model, train, test);
        return new ResponseEntity(Code.SUCCESS.getValue(), new ClassifyEvalResultResp(evalResult), Code.SUCCESS.getDescription());
    }
}
