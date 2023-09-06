package com.daangn.dangunmarket.domain.product.controller.dto;

public record ProductCreateApiResponse(Long memberId) {
    public static ProductCreateApiResponse of(Long memberId){
        return new ProductCreateApiResponse(memberId);
    }
}
