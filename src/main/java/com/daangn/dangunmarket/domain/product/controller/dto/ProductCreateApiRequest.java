package com.daangn.dangunmarket.domain.product.controller.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record ProductCreateApiRequest(
        Long memberId,
        Double latitude,
        Double longitude,
        String alias,
        List<MultipartFile> files,
        Long categoryId,
        String title,
        String content,
        Long price,
        boolean isOfferAllowed,
        LocalDateTime refreshedAt) implements Serializable {
}
