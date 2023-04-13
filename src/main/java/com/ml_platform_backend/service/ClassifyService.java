package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.ClassifyEvalResult;
import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClassifyService {
    @Autowired
    private ClassifyEvalResultService evalService;

    private Instances AttributeNumericToNominal(Instances data, String rangeList) throws Exception {
        NumericToNominal filter = new NumericToNominal();
        filter.setAttributeIndices(rangeList);
        filter.setInputFormat(data);
        return Filter.useFilter(data, filter);
    }

    public ClassifyEvalResult eval(Model model, File trainDataSet, File testDataSet) throws Exception {
        // Load dataset
        Instances train = new ConverterUtils.DataSource(trainDataSet.getFilePath()).getDataSet();
        train.setClassIndex(train.numAttributes() - 1);
        // 将标签列转换为分类属性
        train = AttributeNumericToNominal(train, "last");

        Instances test = new ConverterUtils.DataSource(testDataSet.getFilePath()).getDataSet();
        test.setClassIndex(train.numAttributes() - 1);
        // 将标签列转换为分类属性
        test = AttributeNumericToNominal(test, "last");

        Evaluation eval = new Evaluation(train);
        Classifier classifyModel = getClassifyModel(model);
        eval.evaluateModel(classifyModel, test);

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
        ClassifyEvalResult classifyEvalResult = new ClassifyEvalResult(eval.pctCorrect(), precision, recall, fMeasure, numInstances, (int) eval.numInstances(), model.getId());
        evalService.insert(classifyEvalResult);
        return classifyEvalResult;
    }

    public Classifier getClassifyModel(Model model) throws IOException, ClassNotFoundException {
        // Deserialize model from file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(model.getModelPath()));
        Classifier classifyModel = (Classifier) ois.readObject();
        ois.close();
        return classifyModel;
    }
}
