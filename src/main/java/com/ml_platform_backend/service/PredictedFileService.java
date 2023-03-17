package com.ml_platform_backend.service;

import com.google.gson.Gson;
import com.ml_platform_backend.entry.PredictedFile;
import com.ml_platform_backend.mapper.PredictedFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PredictedFileService {
    @Autowired
    private PredictedFileMapper predictedFileMapper;

    public int insertPredictedFile(PredictedFile predictedFile) {
        return predictedFileMapper.insert(predictedFile);
    }

    public PredictedFile getPredictedFileById(Integer id) {
        return predictedFileMapper.selectById(id);
    }

    public List<Double> getPredictedFileLabelValues(Integer id) {
        List<Double> labelValues = new ArrayList<>();
        PredictedFile predictedFile = getPredictedFileById(id);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(predictedFile.getFilePath()));
            ArffReader arff = new ArffReader(reader);
            Instances instances = arff.getData();
            instances.setClassIndex(instances.numAttributes() - 1);
            for (int i = 0; i < instances.numInstances(); i++) {
                double labelVal = instances.get(i).classValue();
                labelValues.add(labelVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return labelValues;
    }

    public String getFileJsonContent(Integer id) {
        PredictedFile predictedFile = getPredictedFileById(id);
        List<Map<String, Object>> data = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(predictedFile.getFilePath()));
            ArffReader arff = new ArffReader(reader);
            Instances instances = arff.getData();
            for (int i = 0; i < instances.numInstances(); i++) {
                Map<String, Object> instanceMap = new HashMap<>();
                double[] values = instances.get(i).toDoubleArray();
                for (int j = 0; j < instances.numAttributes(); j++) {
                    Attribute attribute = instances.attribute(j);
                    String attributeName = attribute.name();
                    instanceMap.put(attributeName, values[j]);
                }
                data.add(instanceMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.toJson(data);
    }

}
