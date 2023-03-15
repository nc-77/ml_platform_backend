package com.ml_platform_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ml_platform_backend.entry.File;
import com.ml_platform_backend.mapper.FileMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.json.CDL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;

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
        InputStreamReader reader = new InputStreamReader(new FileInputStream(new java.io.File(file.getFilePath())));
        String csvAsString = new BufferedReader(reader).lines().collect(Collectors.joining("\n"));
        return CDL.toJSONArray(csvAsString).toString();
    }
}
