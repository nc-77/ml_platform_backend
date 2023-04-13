package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.entry.vo.FileResp;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.FilterService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterController {
    @Autowired
    private FilterService filterService;
    @Autowired
    private FileService fileService;

    @Data
    static class columnsRemoveReq {
        private Integer fileId;
        private String[] columnsToRemove;
    }

    @PostMapping("/files/removeAttribute")
    public ResponseEntity removeAttribute(@RequestBody columnsRemoveReq req) throws Exception {
        if (req.columnsToRemove == null || req.columnsToRemove.length == 0) {
            return new ResponseEntity(Code.FAILED.getValue(), null, "过滤列为空");
        }
        File outputFile = filterService.removeAttribute(fileService.getFileById(req.fileId), req.columnsToRemove);
        return new ResponseEntity(Code.SUCCESS.getValue(), new FileResp(outputFile), Code.SUCCESS.getDescription());
    }
}
