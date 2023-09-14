package com.daangn.dangunmarket.domain.post.facade.dto;

import org.springframework.data.domain.Pageable;

public record PostSearchRequestParam(
        Integer category,
        Long minPrice,
        Long maxPrice,
        String keyword,
        Pageable pageable
) {
}
