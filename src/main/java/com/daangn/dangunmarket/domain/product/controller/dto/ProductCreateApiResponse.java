package com.daangn.dangunmarket.domain.product.controller.dto;

public record ProductCreateApiResponse(Long memberId) {
    public static ProductCreateApiResponse from(Long memberId) {
        return new ProductCreateApiResponse(memberId);
    }
}
