package com.daangn.dangunmarket.domain.post.controller.dto;

import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponse;

import java.util.List;

public record PostGetApiResponses(
        List<PostGetResponse> contents,
        String userAreaName,
        int totalPages,
        int totalElements,
        int size,
        int page
) {
}
