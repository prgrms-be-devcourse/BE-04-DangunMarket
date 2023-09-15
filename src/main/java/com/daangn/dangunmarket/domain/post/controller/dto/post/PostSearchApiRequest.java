package com.daangn.dangunmarket.domain.post.controller.dto.post;

public record PostSearchApiRequest(
        Integer category,
        Long minPrice,
        Long maxPrice,
        String keyword
) {
}
