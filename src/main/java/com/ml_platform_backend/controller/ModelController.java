package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class ModelController {
    @Autowired
    private ModelService modelService;

    @GetMapping("/models/download/{id}")
    public org.springframework.http.ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) throws IOException {
        Model model = modelService.getModelById(id);
        byte[] data = Files.readAllBytes(Path.of(model.getModelPath()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new org.springframework.http.ResponseEntity<>(data, headers, org.springframework.http.HttpStatus.OK);
    }
}
