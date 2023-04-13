package com.ml_platform_backend.entry.vo;

import com.ml_platform_backend.entry.File;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResp {
    private Integer fileId;
    private String fileName;

    public FileResp(File file) {
        this.fileId = file.getId();
        this.fileName = file.getFileName();
    }
}
