package com.daangn.dangunmarket.domain.post.facade.dto;

import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponse;

import java.util.List;

public record PostSearchResponseParams(
        List<PostGetResponse> contents,
        String userAreaName,
        int totalPages,
        int totalElements,
        int size,
        int page
) {
}
