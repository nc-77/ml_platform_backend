package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.LinearEvalResult;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.mapper.ModelMapper;
import com.ml_platform_backend.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class LinearService {
    @Autowired
    private Utils utils;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LinearEvalResultService evalService;
    @Autowired
    private Options options;

    public void setOptions(Options options) {
        this.options = options;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Component
    public static class Options {
        // 标签列
        private String classIndex;
        // 特征列
        private String[] removeIndex;
    }

    public Model train(File trainDataSet) throws Exception {

        // Load dataset
        DataSource source = new DataSource(trainDataSet.getFilePath());
        Instances data = source.getDataSet();


        // 过滤无效的特征列
        if (options.removeIndex != null && options.removeIndex.length != 0) {
            List<Integer> removeAttributeIndices = new ArrayList<>();
            for (String removeIndexName : options.removeIndex) {
                removeAttributeIndices.add(data.attribute(removeIndexName).index());
            }
            Remove remove = new Remove();
            remove.setAttributeIndicesArray(removeAttributeIndices.stream().mapToInt(Integer::intValue).toArray());
            remove.setInputFormat(data);
            data = Filter.useFilter(data, remove);
        }


        // 选择标签列
        if (options.classIndex != null && !options.classIndex.isEmpty()) {
            data.setClassIndex(data.attribute(options.classIndex).index());
        }
        if (data.classIndex() == -1) {
            data.setClassIndex(data.numAttributes() - 1);
        }

        // Build linear regression model
        LinearRegression model = new LinearRegression();
        model.buildClassifier(data);

        String modelName = utils.getBaseName(trainDataSet.getFileName()) + ".model";
        String currentDirectory = System.getProperty("user.dir");
        Path modelPath = Paths.get(currentDirectory + "/models/" + modelName);
        Model modelEntry = new Model(modelName, modelPath.toString(), model.getClass().toString(), trainDataSet.getId());

        // Serialize model to file
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelEntry.getModelPath()));
        oos.writeObject(model);
        oos.close();

        // save trained model to db
        int result = modelMapper.insert(modelEntry);
        if (result == 0) {
            return null;
        }
        return modelEntry;
    }

    public LinearEvalResult evalModel(Model model, File trainDataSet, File testDataSet) throws Exception {
        // Load dataset
        Instances train = new DataSource(trainDataSet.getFilePath()).getDataSet();
        train.setClassIndex(train.numAttributes() - 1);
        Instances test = new DataSource(testDataSet.getFilePath()).getDataSet();
        test.setClassIndex(train.numAttributes() - 1);

        Evaluation eval = new Evaluation(train);
        LinearRegression linearModel = getLinearModel(model);
        eval.evaluateModel(linearModel, test);

        // save evalResult to db
        LinearEvalResult linearEvalResult = new LinearEvalResult(eval.correlationCoefficient(), eval.meanAbsoluteError(), eval.rootMeanSquaredError(), eval.relativeAbsoluteError(), eval.rootRelativeSquaredError(), (int) eval.numInstances());
        evalService.insert(linearEvalResult);
        return linearEvalResult;
    }

    public LinearRegression getLinearModel(Model model) throws IOException, ClassNotFoundException {
        // Deserialize model from file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(model.getModelPath()));
        LinearRegression linearModel = (LinearRegression) ois.readObject();
        ois.close();
        return linearModel;
    }
}
