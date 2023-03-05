package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedWriter;
import java.io.FileWriter;

@Service
public class PredictService {
    @Autowired
    private Utils utils;
    @Autowired
    private LinearService linearService;
    @Autowired
    private FileService fileService;

    public File predictLinearModel(Model model, File testDataSet) throws Exception {
        LinearRegression linearModel = linearService.getLinearModel(model);
        // load unlabeled data
        DataSource source = new DataSource(testDataSet.getFilePath());
        Instances unlabeled = source.getDataSet();

        // set class attribute
        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);

        // create copy
        Instances labeled = new Instances(unlabeled);

        // label instances
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            double clsLabel = linearModel.classifyInstance(unlabeled.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }

        // save labeled data to fileSys
        String savedPath = utils.getBaseName(testDataSet.getFilePath()) + "-predicted.arff";
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(savedPath));
        writer.write(labeled.toString());
        writer.newLine();
        writer.flush();
        writer.close();

        // save labeled data to db
        File labeledFile = new File(utils.getBaseName(testDataSet.getFileName()) + "-predicted", savedPath);
        fileService.insertFile(labeledFile);
        return labeledFile;
    }
}
