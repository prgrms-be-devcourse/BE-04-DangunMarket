package com.daangn.dangunmarket.domain.post.service.dto;

import org.springframework.data.domain.Pageable;

public record PostSearchConditionRequest(
        Integer category,
        Long minPrice,
        Long maxPrice,
        String keyword,
        Pageable pageable
) {
}
