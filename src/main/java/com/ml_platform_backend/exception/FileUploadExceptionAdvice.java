package com.ml_platform_backend.exception;

import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class FileUploadExceptionAdvice {
    @ExceptionHandler({MultipartException.class, MissingServletRequestPartException.class})
    public ResponseEntity handleMultipartException() {
        return new ResponseEntity(Code.UPLOAD_ERR.getValue(), null, "上传文件为空");
    }
}
