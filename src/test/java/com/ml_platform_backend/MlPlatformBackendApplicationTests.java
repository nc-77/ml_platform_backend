package com.ml_platform_backend;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.service.FileService;
import com.ml_platform_backend.service.LinearService;
import com.ml_platform_backend.service.ModelService;
import com.ml_platform_backend.service.PredictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;

@SpringBootTest
class MlPlatformBackendApplicationTests {
    @Autowired
    private LinearService linearService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private PredictService predictService;

    @Test
    void TestLinearServiceTrain() throws Exception {
        File dataSource = fileService.getFileById(1825833034);
        linearService.train(dataSource);
    }

    @Test
    void TestPredictService() throws Exception {
        Model model = modelService.getModelById(1825833047);
        File testDataSet = fileService.getFileById(1825833035);
        predictService.predictLinearModel(model, testDataSet);
    }

    @Test
    void TestLinearServiceEvalModel() throws Exception {
        Model model = modelService.getModelById(1825833054);
        File train = fileService.getFileById(1825833052);
        File test = fileService.getFileById(1825833053);
        linearService.evalModel(model, train, test);
    }

    @Test
    void TestgetFileJsonContentService() throws FileNotFoundException {
        String json = fileService.getFileJsonContent(1825832980);
        System.out.println(json);
    }
}
