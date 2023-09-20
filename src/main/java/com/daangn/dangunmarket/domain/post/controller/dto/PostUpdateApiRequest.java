package com.daangn.dangunmarket.domain.post.controller.dto;

import com.daangn.dangunmarket.domain.post.controller.annotation.FileSize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostUpdateApiRequest(
        @NotNull Long postId,
        Double latitude,
        Double longitude,
        String alias,
        @FileSize PostImageUpdateApiRequest postImageUpdateApiRequest,
        @NotNull(message = "카테고리를 선택해주셔야 합니다.") Long categoryId,
        @NotBlank(message = "제목을 채워주시기 바랍니다.") String title,
        @NotBlank(message = "내용을 빈 칸일 수 없습니다.") String content,
        @Min(value = 0, message = "가경은 음수가 될 수 없습니다.")long price,
        boolean isOfferAllowed
) {

    public List<MultipartFile> getFiles() {
        return postImageUpdateApiRequest.files();
    }

    public List<String> getUrls() {
        return postImageUpdateApiRequest.urls();
    }

}
