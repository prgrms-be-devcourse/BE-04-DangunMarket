package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.repository.dto.PostDto;
import org.springframework.data.domain.Slice;

import java.util.List;

public record PostGetResponses(
        List<PostGetResponse> contents,
        int size, //페이지 크기
        int page, //현재 페이지
        boolean hasNext
) {

    public static PostGetResponses from(Slice<PostDto> postDtos) {

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
                postDtos.getSize(),
                postDtos.getNumber(),
                postDtos.hasNext()
        );
    }

}
