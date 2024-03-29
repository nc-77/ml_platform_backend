package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class KnnService {
    @Autowired
    private Utils utils;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ClassifyEvalResultService evalService;

    private Instances AttributeNumericToNominal(Instances data, String rangeList) throws Exception {
        NumericToNominal filter = new NumericToNominal();
        filter.setAttributeIndices(rangeList);
        filter.setInputFormat(data);
        return Filter.useFilter(data, filter);
    }

    public Model train(File trainDataSet, Integer k) throws Exception {
        // Load dataset
        DataSource source = new DataSource(trainDataSet.getFilePath());
        Instances data = source.getDataSet();

        // set class index
        data.setClassIndex(data.numAttributes() - 1);

        // 将标签列转换为分类属性
        Instances newData = AttributeNumericToNominal(data, "last");

        // create knn classifier
        IBk knn = new IBk();
        knn.setKNN(k);
        // build model
        knn.buildClassifier(newData);

        // new modelEntry
        String modelName = utils.getBaseName(trainDataSet.getFileName()) + ".model";
        String currentDirectory = System.getProperty("user.dir");
        Path modelPath = Paths.get(currentDirectory + "/models/" + modelName);
        Model modelEntry = new Model(modelName, modelPath.toString(), knn.getClass().toString(), trainDataSet.getId());

        // Serialize model to file
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelEntry.getModelPath()));
        oos.writeObject(knn);
        oos.close();

        // save trained model to db
        modelService.insertModel(modelEntry);
        return modelEntry;
    }

    public IBk getKnnModel(Model model) throws IOException, ClassNotFoundException {
        // Deserialize model from file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(model.getModelPath()));
        IBk KNNModel = (IBk) ois.readObject();
        ois.close();
        return KNNModel;
    }

}
