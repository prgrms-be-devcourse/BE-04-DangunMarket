package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record PostGetResponses(
        List<PostGetResponse> contents,
        int totalPages,
        long totalElements,
        int size,
        int page
) {

    public static PostGetResponses from(Page<PostDto> postDtos) {

        List<PostGetResponse> posts = postDtos.stream()
                .map(
                        dto -> new PostGetResponse(
                                dto.post().getId(),
                                dto.post().getTitle(),
                                dto.post().getTitle(),
                                dto.post().getCreatedAt(),
                                dto.post().getRefreshedAt(),
                                dto.post().getPrice(),
                                dto.post().getLikeCount(),
                                dto.post().getTradeStatus(),
                                dto.post().getPostImages().isEmpty() ? null : dto.post().getPostImages().get(0).getUrl(),
                                dto.areaName()
                        )
                )
                .toList();

        return new PostGetResponses(posts,
                postDtos.getTotalPages(),
                postDtos.getTotalElements(),
                postDtos.getSize(),
                postDtos.getNumber());
    }

}
