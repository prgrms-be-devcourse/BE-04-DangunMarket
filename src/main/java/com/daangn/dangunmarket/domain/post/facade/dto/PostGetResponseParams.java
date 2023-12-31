package com.daangn.dangunmarket.domain.post.facade.dto;

import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponse;

import java.util.List;

public record PostGetResponseParams(
        List<PostGetResponse> contents,
        String userAreaName,
        int size,
        int page,
        boolean hasNext
) {
}
