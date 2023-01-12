package com.ml_platform_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadSvc {
    public boolean handleFileUpload(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String currentDirectory = System.getProperty("user.dir");
            Path path = Paths.get(currentDirectory + "/dataSources/" + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
