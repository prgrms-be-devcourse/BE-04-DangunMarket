package com.daangn.dangunmarket.domain.chat.controller.dto;

import com.daangn.dangunmarket.global.aws.dto.ImageInfo;

import java.util.List;

public record ChatImageUploadApiResponse(
        List<String> urls
) {
    public static ChatImageUploadApiResponse from(List<ImageInfo> imageInfos) {
        return new ChatImageUploadApiResponse(imageInfos
                .stream()
                .map(ImageInfo::url)
                .toList());
    }
}
