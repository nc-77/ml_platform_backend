package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.result.Result;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.service.FileUploadSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class FileUploadController {
    @Autowired
    private FileUploadSvc fileUploadSvc;

    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        boolean success = fileUploadSvc.handleFileUpload(file);
        if (success) {
            return new Result(Code.UPLOAD_OK, null, Code.UPLOAD_OK.getDescription());
        }
        return new Result(Code.UPLOAD_ERR, null, Code.UPLOAD_ERR.getDescription());
    }
}
