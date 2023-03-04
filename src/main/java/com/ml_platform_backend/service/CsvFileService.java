package com.ml_platform_backend.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CsvFileService {

    public List<String[]> readCsv(String filePath) throws IOException, CsvValidationException {
        List<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
        }
        return data;
    }

    public ImmutablePair<List<String[]>, List<String[]>> splitDataset(List<String[]> data, double splitSize, boolean ignoredFirstLine, Integer randomSeed) {
        List<String[]> trainingSet = new ArrayList<>();
        List<String[]> testSet = new ArrayList<>();
        Random random = new Random(randomSeed);

        if (ignoredFirstLine) {
            String[] firstLine = data.get(0);
            data = data.subList(1, data.size());
            trainingSet.add(firstLine);
            testSet.add(firstLine);
        }
        for (String[] row : data) {
            if (random.nextDouble() < splitSize) {
                trainingSet.add(row); // 添加到训练集
            } else {
                testSet.add(row); // 添加到测试集
            }
        }
        return new ImmutablePair<>(trainingSet, testSet);
    }

    public void writeCsv(List<String[]> data, String filePath) throws IOException {
        File file = new File(filePath);
        FileWriter outputFile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputFile);
        writer.writeAll(data);
        writer.close();
    }
}
