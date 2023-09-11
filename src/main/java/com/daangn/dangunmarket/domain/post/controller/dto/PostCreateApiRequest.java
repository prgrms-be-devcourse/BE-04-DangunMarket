package com.daangn.dangunmarket.domain.post.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record PostCreateApiRequest(
        @NotNull(message = "areaId가 누락되었습니다.") Long areaId,
        @NotNull(message = "위도가 누락되었습니다.") Double latitude,
        @NotNull(message = "경도가 누락되었습니다.") Double longitude,
        @NotNull(message = "alias가 누락되었습니다.") String alias,
        List<MultipartFile> files,
        Long categoryId,
        @NotBlank(message = "title이 누락되었거나 비었습니다.") String title,
        @NotBlank(message = "content가 누락되었거나 비었습니다.") String content,
        @Min(value = 0, message = "price는 0원 이하가 될 수 없습니다.") long price,
        boolean isOfferAllowed,
        LocalDateTime refreshedAt) implements Serializable {
}
