package com.ml_platform_backend;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.LinearService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MlPlatformBackendApplicationTests {
    @Autowired
    private LinearService linearService;
    @Autowired
    private FileService fileService;

    @Test
    void TestLinearService() throws Exception {
        File dataSource = fileService.getAllPredFiles().get(0);
        linearService.LinearRegressionTrain(dataSource);
    }

}
