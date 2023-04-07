package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.mapper.FileMapper;
import com.ml_platform_backend.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private Utils utils;

    public File handleFileUpload(@NotNull MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String currentDirectory = System.getProperty("user.dir");
            String fileName = file.getOriginalFilename();
            String newFilename = utils.getFileRandomName(fileName);
            Path path = Paths.get(currentDirectory + "/dataSources/" + newFilename);
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
            File savedfile = new File(newFilename, path.toString());
            fileMapper.insert(savedfile);
            return savedfile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
