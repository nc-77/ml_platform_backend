package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LogisticService {
    @Autowired
    private Utils utils;
    @Autowired
    private ClassifyEvalResultService evalService;
    @Autowired
    private ModelService modelService;

    private Instances AttributeNumericToNominal(Instances data, String rangeList) throws Exception {
        NumericToNominal filter = new NumericToNominal();
        filter.setAttributeIndices(rangeList);
        filter.setInputFormat(data);
        return Filter.useFilter(data, filter);
    }

    public Model train(File trainDataSet) throws Exception {
        // Load dataset
        DataSource source = new DataSource(trainDataSet.getFilePath());
        Instances data = source.getDataSet();

        // set class index
        data.setClassIndex(data.numAttributes() - 1);

        // 将标签列转换为分类属性
        if (data.attribute(data.numAttributes() - 1).isNumeric()) {
            data = AttributeNumericToNominal(data, "last");
        }

        // build model
        Logistic logistic = new Logistic();
        logistic.buildClassifier(data);

        // new modelEntry
        String modelName = utils.getBaseName(trainDataSet.getFileName()) + ".model";
        String currentDirectory = System.getProperty("user.dir");
        Path modelPath = Paths.get(currentDirectory + "/models/" + modelName);
        Model modelEntry = new Model(modelName, modelPath.toString(), logistic.getClass().toString(), trainDataSet.getId());

        // Serialize model to file
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelEntry.getModelPath()));
        oos.writeObject(logistic);
        oos.close();

        // save trained model to db
        modelService.insertModel(modelEntry);
        return modelEntry;
    }

    public Logistic getLogisticModel(Model model) throws IOException, ClassNotFoundException {
        // Deserialize model from file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(model.getModelPath()));
        Logistic logisticModel = (Logistic) ois.readObject();
        ois.close();
        return logisticModel;
    }
}
