package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.entry.Model;
import com.ml_platform_backend.mapper.ModelMapper;
import com.ml_platform_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class KNNService {
    @Autowired
    private Utils utils;
    @Autowired
    private ModelMapper modelMapper;

    public Model train(File trainDataSet) throws Exception {
        // Load dataset
        DataSource source = new DataSource(trainDataSet.getFilePath());
        Instances data = source.getDataSet();

        // set class index
        data.setClassIndex(data.numAttributes() - 1);

        // create knn classifier
        IBk knn = new IBk();
        // build model
        knn.buildClassifier(data);

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
        int result = modelMapper.insert(modelEntry);
        if (result == 0) {
            return null;
        }
        return modelEntry;
    }

    public IBk getKNNModel(Model model) throws IOException, ClassNotFoundException {
        // Deserialize model from file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(model.getModelPath()));
        IBk KNNModel = (IBk) ois.readObject();
        ois.close();
        return KNNModel;
    }

}
