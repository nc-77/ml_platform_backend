package com.ml_platform_backend.service;

import com.ml_platform_backend.entry.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

@Service
public class FilterService {
    @Autowired
    private FileService fileService;

    public File removeAttribute(File file, String[] columnsToRemove) throws Exception {
        // Load dataset
        DataSource source = new DataSource(file.getFilePath());
        Instances data = source.getDataSet();

        // 将列名转换为列索引
        int[] indicesToRemove = new int[columnsToRemove.length];
        for (int i = 0; i < columnsToRemove.length; i++) {
            indicesToRemove[i] = data.attribute(columnsToRemove[i]).index();
        }

        // 创建过滤器对象
        Remove remove = new Remove();
        remove.setAttributeIndicesArray(indicesToRemove);
        remove.setInputFormat(data);

        // 应用过滤器
        Instances filteredData = Filter.useFilter(data, remove);
        File outputFile = fileService.getNewFile(file);

        // 将处理后的数据集保存为CSV格式
        CSVSaver saver = new CSVSaver();
        saver.setInstances(filteredData);
        saver.setFile(new java.io.File(outputFile.getFilePath()));
        saver.writeBatch();

        // save output file to db
        fileService.insertFile(outputFile);
        return outputFile;
    }
}
