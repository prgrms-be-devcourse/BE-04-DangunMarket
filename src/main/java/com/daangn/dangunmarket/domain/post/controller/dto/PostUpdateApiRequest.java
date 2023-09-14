package com.daangn.dangunmarket.domain.post.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostUpdateApiRequest (
        @NotNull Long postId,
        @NotNull(message = "위도가 누락되었습니다.") Double latitude,
        @NotNull(message = "경도가 누락되었습니다.") Double longitude,
        @NotNull(message = "alias가 누락되었습니다") String alias,
        @Size(max = 3,message = "사진은 최대 3개까지만 가능합니다.") List<MultipartFile> files,
        @NotNull(message = "카테고리를 선택해주셔야 합니다.") Long categoryId,
        @NotBlank(message = "제목을 채워주시기 바랍니다.") String title,
        @NotBlank(message = "내용을 빈 칸일 수 없습니다.") String content,
        @Min(value = 0, message = "가경은 음수가 될 수 없습니다.") long price,
        boolean isOfferAllowed
) {
}