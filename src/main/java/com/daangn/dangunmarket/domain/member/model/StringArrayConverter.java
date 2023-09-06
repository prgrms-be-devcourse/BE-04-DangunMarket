package com.daangn.dangunmarket.domain.member.model;

import com.daangn.dangunmarket.domain.member.model.EvaluationItem;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringArrayConverter implements AttributeConverter<List<EvaluationItem>,String> {

    private static final String SPLIT_CHAR = ",";


    @Override
    public String convertToDatabaseColumn(List<EvaluationItem> attribute) {
        return attribute.stream().map(e->e.getEvaluationDetail()).collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<EvaluationItem> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(EvaluationItem::findByEvaluationItem)
                .collect(Collectors.toList());
    }
}
