package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.mapper.FileMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadSvc {
    @Autowired
    private FileMapper fileMapper;

    public File handleFileUpload(@NotNull MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String currentDirectory = System.getProperty("user.dir");
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(currentDirectory + "/dataSources/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
            File savedfile = new File(fileName, path.toString());
            fileMapper.insert(savedfile);
            return savedfile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
