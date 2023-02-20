package com.ml_platform_backend.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class File {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String fileName;
    private String filePath;

    public File(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
