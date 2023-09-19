package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.repository.postlike.dto.JoinedPostWithArea;
import org.springframework.data.domain.Slice;

public record LikedPostFindResponseList(Slice<LikedPostFindResponse> likedPosts) {

    public static LikedPostFindResponseList from(Slice<JoinedPostWithArea> joinedWithAreas) {
        Slice<LikedPostFindResponse> mapResponse = joinedWithAreas.map(e -> new LikedPostFindResponse(
                e.post().getTitle(),
                e.post().getPostImages().isEmpty() ? "" : e.post().getPostImages().get(0).getUrl(),
                e.area().getName(),
                e.post().getPrice(),
                e.post().getLikeCount()));
        return new LikedPostFindResponseList(mapResponse);
    }

}
