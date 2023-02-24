package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.FileUploadSvc;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;


@RestController
public class FileUploadController {
    @Autowired
    private FileUploadSvc fileUploadSvc;

    @Data
    @AllArgsConstructor
    private class respData {
        private Integer fileId;
        private String fileName;
    }

    @ExceptionHandler({MultipartException.class, MissingServletRequestPartException.class})
    public ResponseEntity handleMultipartException() {
        return new ResponseEntity(Code.UPLOAD_ERR.getValue(), null, "上传文件为空");
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity(Code.UPLOAD_ERR.getValue(), null, "文件为空");
        }
        File savedFile = fileUploadSvc.handleFileUpload(file);
        if (savedFile != null) {
            return new ResponseEntity(Code.UPLOAD_OK.getValue(), new respData(savedFile.getId(), savedFile.getFileName()), Code.UPLOAD_OK.getDescription());
        }
        return new ResponseEntity(Code.UPLOAD_ERR.getValue(), null, Code.UPLOAD_ERR.getDescription());
    }
}
