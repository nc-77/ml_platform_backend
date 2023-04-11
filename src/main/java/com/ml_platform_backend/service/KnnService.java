package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.KnnEvalResult;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class KnnService {
    @Autowired
    private Utils utils;
    @Autowired
    private ModelService modelService;
    @Autowired
    private KnnEvalResultService evalService;

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

    public KnnEvalResult eval(Model model, File trainDataSet, File testDataSet) throws Exception {
        // Load dataset
        Instances train = new DataSource(trainDataSet.getFilePath()).getDataSet();
        train.setClassIndex(train.numAttributes() - 1);
        // 将标签列转换为分类属性
        train = AttributeNumericToNominal(train, "last");

        Instances test = new DataSource(testDataSet.getFilePath()).getDataSet();
        test.setClassIndex(train.numAttributes() - 1);
        // 将标签列转换为分类属性
        test = AttributeNumericToNominal(test, "last");

        Evaluation eval = new Evaluation(train);
        IBk knn = getKnnModel(model);
        eval.evaluateModel(knn, test);

        Map<String, Double> precision = new HashMap<>();
        Map<String, Double> recall = new HashMap<>();
        Map<String, Double> fMeasure = new HashMap<>();
        Map<String, Integer> numInstances = new HashMap<>();
        for (int i = 0; i < train.numClasses(); i++) {
            String labelName = train.classAttribute().value(i);
            precision.put(labelName, eval.precision(i));
            recall.put(labelName, eval.recall(i));
            fMeasure.put(labelName, eval.fMeasure(i));
        }
        for (int i = 0; i < test.numInstances(); i++) {
            String labelName = test.instance(i).stringValue(test.classIndex());
            if (numInstances.containsKey(labelName)) {
                numInstances.put(labelName, numInstances.get(labelName) + 1);
            } else {
                numInstances.put(labelName, 1);
            }
        }
        // save evaluation result to db
        KnnEvalResult knnEvalResult = new KnnEvalResult(eval.pctCorrect(), precision, recall, fMeasure, numInstances, (int) eval.numInstances(), model.getId());
        evalService.insert(knnEvalResult);
        return knnEvalResult;
    }

    public IBk getKnnModel(Model model) throws IOException, ClassNotFoundException {
        // Deserialize model from file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(model.getModelPath()));
        IBk KNNModel = (IBk) ois.readObject();
        ois.close();
        return KNNModel;
    }

}
