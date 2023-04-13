package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.entry.PredictedFile;
import com.ml_platform_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PredictService {
    @Autowired
    private Utils utils;
    @Autowired
    private LinearService linearService;
    @Autowired
    private KnnService knnService;
    @Autowired
    private LogisticService logisticService;
    @Autowired
    private PredictedFileService predictedFileService;

    private void savePredictedFileToFileSys(Instances predictedFile, Path filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath.toString()));
        writer.write(predictedFile.toString());
        writer.newLine();
        writer.flush();
        writer.close();
    }

    public PredictedFile predictLinearModel(Model model, File testDataSet) throws Exception {
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
        String currentDirectory = System.getProperty("user.dir");
        String fileName = utils.getBaseName(testDataSet.getFileName()) + "-predicted.arff";
        Path savedPath = Paths.get(currentDirectory + "/predictedFiles/" + fileName);
        savePredictedFileToFileSys(labeled, savedPath);

        // save labeled data to db
        PredictedFile labeledFile = new PredictedFile(fileName, savedPath.toString(), model.getId(), testDataSet.getId());
        predictedFileService.insertPredictedFile(labeledFile);
        return labeledFile;
    }

    public PredictedFile predictKNNModel(Model model, File testDataSet) throws Exception {
        IBk knn = knnService.getKnnModel(model);
        // load unlabeled data
        DataSource source = new DataSource(testDataSet.getFilePath());
        Instances unlabeled = source.getDataSet();

        // set class attribute
        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
        // 将标签列转换为分类属性
        NumericToNominal filter = new NumericToNominal();
        filter.setAttributeIndices("last");
        filter.setInputFormat(unlabeled);
        unlabeled = Filter.useFilter(unlabeled, filter);

        // Predict class labels for test instances
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            Instance instance = unlabeled.instance(i);
            double predictedLabel = knn.classifyInstance(instance);
            //String trueLabel = instance.stringValue(instance.classAttribute());
            String predictedLabelString = unlabeled.classAttribute().value((int) predictedLabel);
            unlabeled.instance(i).setClassValue(predictedLabelString);
            //System.out.println("Actual: " + trueLabel + " Predicted: " + predictedLabelString);
        }

        // save labeled data to fileSys
        String currentDirectory = System.getProperty("user.dir");
        String fileName = utils.getBaseName(testDataSet.getFileName()) + "-predicted.arff";
        Path savedPath = Paths.get(currentDirectory + "/predictedFiles/" + fileName);
        savePredictedFileToFileSys(unlabeled, savedPath);

        // save labeled data to db
        PredictedFile labeledFile = new PredictedFile(fileName, savedPath.toString(), model.getId(), testDataSet.getId());
        predictedFileService.insertPredictedFile(labeledFile);
        return labeledFile;
    }

    public PredictedFile predictLogisticModel(Model model, File testDataSet) throws Exception {
        Logistic logistic = logisticService.getLogisticModel(model);
        // load unlabeled data
        DataSource source = new DataSource(testDataSet.getFilePath());
        Instances unlabeled = source.getDataSet();

        // set class attribute
        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
        // 将标签列转换为分类属性
        NumericToNominal filter = new NumericToNominal();
        filter.setAttributeIndices("last");
        filter.setInputFormat(unlabeled);
        unlabeled = Filter.useFilter(unlabeled, filter);

        // Predict class labels for test instances
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            Instance instance = unlabeled.instance(i);
            double predictedLabel = logistic.classifyInstance(instance);
            //String trueLabel = instance.stringValue(instance.classAttribute());
            String predictedLabelString = unlabeled.classAttribute().value((int) predictedLabel);
            unlabeled.instance(i).setClassValue(predictedLabelString);
            //System.out.println("Actual: " + trueLabel + " Predicted: " + predictedLabelString);
        }

        // save labeled data to fileSys
        String currentDirectory = System.getProperty("user.dir");
        String fileName = utils.getBaseName(testDataSet.getFileName()) + "-predicted.arff";
        Path savedPath = Paths.get(currentDirectory + "/predictedFiles/" + fileName);
        savePredictedFileToFileSys(unlabeled, savedPath);

        // save labeled data to db
        PredictedFile labeledFile = new PredictedFile(fileName, savedPath.toString(), model.getId(), testDataSet.getId());
        predictedFileService.insertPredictedFile(labeledFile);
        return labeledFile;
    }
}
