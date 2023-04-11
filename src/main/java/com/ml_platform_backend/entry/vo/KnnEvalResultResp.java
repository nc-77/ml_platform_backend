package com.ml_platform_backend.entry.vo;

import com.ml_platform_backend.entry.BaseEntity;
import com.ml_platform_backend.entry.KnnEvalResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class KnnEvalResultResp extends BaseEntity {
    private Double pctCorrect;
    private List<KnnLabelEvalResult> labelEvalResults;
    private int totalNumInstances;

    public KnnEvalResultResp(KnnEvalResult evalResult) {
        List<KnnLabelEvalResult> labelEvalResults = new ArrayList<>();
        for (String label : evalResult.getPrecisionRate().keySet()) {
            KnnLabelEvalResult labelEvalResult = new KnnLabelEvalResult(label);
            labelEvalResult.setPrecisionRate(evalResult.getPrecisionRate().get(label));
            labelEvalResult.setRecall(evalResult.getRecall().get(label));
            labelEvalResult.setFMeasure(evalResult.getFMeasure().get(label));
            labelEvalResult.setNumInstances(evalResult.getNumInstances().get(label));
            labelEvalResults.add(labelEvalResult);
        }
        this.id = evalResult.getId();
        this.createdAt = evalResult.getCreatedAt();
        this.updatedAt = evalResult.getUpdatedAt();
        this.pctCorrect = evalResult.getPctCorrect();
        this.labelEvalResults = labelEvalResults;
        this.totalNumInstances = evalResult.getTotalNumInstances();
    }
}

