package com.ml_platform_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.mapper.FileMapper;
import com.ml_platform_backend.utils.Utils;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.json.CDL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.instance.RemoveDuplicates;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private Utils utils;

    public File getNewFile(File file) {
        // 为fileName添加随机后缀
        String fileName = file.getFileName();
        String newFileName = utils.getFileRandomName(fileName);
        Path path = Paths.get(file.getFilePath());
        Path newFilePath = Paths.get(path.getParent().toString(), newFileName);
        return new File(newFileName, newFilePath.toString(), file.getId());
    }

    public File getFileById(Integer id) {
        return fileMapper.selectById(id);
    }

    public File getTestFile(Integer trainFileId) {
        return fileMapper.selectById(trainFileId + 1);
    }

    public List<File> getAllPredFiles() {
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_pre", 1);
        return fileMapper.selectList(queryWrapper);
    }

    public void insertFile(File file) {
        fileMapper.insert(file);
    }

    public String[] getFirstLine(Integer id) throws IOException, CsvValidationException {
        File file = getFileById(id);
        CSVReader csvReader = new CSVReader(new FileReader(file.getFilePath()));
        return csvReader.readNext();
    }

    public String getFileJsonContent(Integer id) throws FileNotFoundException {
        File file = getFileById(id);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file.getFilePath()));
        String csvAsString = new BufferedReader(reader).lines().collect(Collectors.joining("\n"));
        return CDL.toJSONArray(csvAsString).toString();
    }

    // 文件去重
    public File removeDuplicate(File file) throws Exception {
        // 读取CSV文件并将其转换为数据集对象
        CSVLoader loader = new CSVLoader();
        loader.setSource(new java.io.File(file.getFilePath()));
        Instances data = loader.getDataSet();

        // 使用RemoveDuplicates类对数据集进行去重处理
        RemoveDuplicates filter = new RemoveDuplicates();
        filter.setInputFormat(data);
        Instances deduplicatedData = Filter.useFilter(data, filter);
        File savedFile = getNewFile(file);

        // 将处理后的数据集保存为CSV格式
        CSVSaver saver = new CSVSaver();
        saver.setInstances(deduplicatedData);
        saver.setFile(new java.io.File(savedFile.getFilePath()));
        saver.writeBatch();
        // 保存处理后的数据集至数据库
        insertFile(savedFile);

        return savedFile;
    }

    // 处理缺失值
    public File handleMissingValues(File file, String handleMethod) throws Exception {
        // 读取CSV文件并将其转换为数据集对象
        CSVLoader loader = new CSVLoader();
        loader.setSource(new java.io.File(file.getFilePath()));
        Instances data = loader.getDataSet();

        if (handleMethod.equals("remove")) {
            // 过滤掉含有缺失值的行
            Instances filteredData = new Instances(data, 0); // 创建一个空的数据集
            for (int i = 0; i < data.numInstances(); i++) {
                Instance inst = data.instance(i);
                boolean hasMissingValue = false;
                for (int j = 0; j < inst.numAttributes(); j++) {
                    if (inst.isMissing(j)) {
                        hasMissingValue = true;
                        break;
                    }
                }
                if (!hasMissingValue) {
                    filteredData.add(inst);
                }
            }
            data = filteredData;
        } else {
            // 填充缺失值
            ReplaceMissingValues filler = new ReplaceMissingValues();
            filler.setInputFormat(data);
            data = Filter.useFilter(data, filler);
        }

        // 将处理后的数据集保存为CSV格式
        File savedFile = getNewFile(file);
        CSVSaver saver = new CSVSaver();
        saver.setInstances(data);
        saver.setFile(new java.io.File(savedFile.getFilePath()));
        saver.writeBatch();
        // 保存处理后的数据集至数据库
        insertFile(savedFile);

        return savedFile;
    }
}
