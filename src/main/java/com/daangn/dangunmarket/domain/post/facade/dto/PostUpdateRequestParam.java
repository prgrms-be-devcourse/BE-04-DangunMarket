package com.daangn.dangunmarket.domain.post.facade.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostUpdateRequestParam(
        Long postId,
        Double latitude,
        Double longitude,
        String alias,
        List<MultipartFile> files,
        List<String> urls,
        Long categoryId,
        String title,
        String content,
        long price,
        boolean isOfferAllowed) {
}
