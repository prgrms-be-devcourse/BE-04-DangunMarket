package com.daangn.dangunmarket.domain.post.controller.dto.post;

import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponse;

import java.util.List;

public record PostSearchApiResponses(
        List<PostGetResponse> contents,
        String userAreaName,
        int size,
        int page,
        boolean hasNext
) {
}
