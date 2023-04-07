package com.ml_platform_backend.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class File extends BaseEntity {
    private String fileName;
    private String filePath;
    private boolean isPre;
    private Integer originFileId;

    public File(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public File(String fileName, String filePath, Integer originFileId) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.originFileId = originFileId;
    }

}
