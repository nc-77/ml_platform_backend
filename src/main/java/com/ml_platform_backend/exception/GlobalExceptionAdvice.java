package com.ml_platform_backend.exception;

import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException() {
        return new ResponseEntity(Code.FAILED.getValue(), null, Code.FAILED.getDescription());
    }
}
