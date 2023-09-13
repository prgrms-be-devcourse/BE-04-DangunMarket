package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record PostSearchResponses(
        List<PostSearchResponse> contents,
        int totalPages,
        long totalElements,
        int size,
        int page
) {

    public static PostSearchResponses from(Page<PostDto> postDtos) {
        List<PostSearchResponse> posts = postDtos.stream()
                .map(
                        dto -> new PostSearchResponse(
                                dto.post().getId(),
                                dto.post().getTitle(),
                                dto.post().getTitle(),
                                dto.post().getCreatedAt(),
                                dto.post().getRefreshedAt(),
                                dto.post().getPrice(),
                                dto.post().getLikeCount(),
                                dto.post().getTradeStatus(),
                                dto.post().getPostImageList().isEmpty() ? null : dto.post().getPostImageList().get(0).getUrl(),
                                dto.areaName()
                        )
                )
                .collect(Collectors.toList());

        return new PostSearchResponses(posts,
                postDtos.getTotalPages(),
                postDtos.getTotalElements(),
                postDtos.getSize(),
                postDtos.getNumber());
    }

}
