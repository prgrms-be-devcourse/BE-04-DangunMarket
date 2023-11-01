package com.daangn.dangunmarket.domain.member.model;

import java.util.Arrays;

public enum EvaluationItem {
    DESCRIPTION_EQUAL_PRODUCT_STATE("상품상태가 설명한 것과 같아요",10),
    FREE_PRODUCT("나눔을 해주셨어요",10),
    DETAIL_DESCRIPTION("상품설명이 자세해요",10),
    CHEAP_GOOD_PRODUCT("좋은 상품을 저렴하게 판매해요",10),
    GOOD_PROMISE("시간 약속을 잘 지켜요",10)
    ;

    private final String evaluationDetail;
    private final int score;

    EvaluationItem(String evaluationDetail, int score) {
        this.evaluationDetail = evaluationDetail;
        this.score = score;
    }

    public String getEvaluationDetail() {
        return evaluationDetail;
    }

    public int getScore() {
        return score;
    }

    public static EvaluationItem findByEvaluationItem (String item){
        return Arrays.stream(EvaluationItem.values())
                .filter(e->e.evaluationDetail.equals(item))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("제공하는 리뷰 항목이 아닙니다."));
    }
}
