package com.ml_platform_backend.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File extends BaseEntity {
    private String fileName;
    private String filePath;
    private boolean isPre;

    public File(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

}