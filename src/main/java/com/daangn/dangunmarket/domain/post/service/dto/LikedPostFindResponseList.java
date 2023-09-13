package com.daangn.dangunmarket.domain.post.service.dto;

import com.daangn.dangunmarket.domain.post.repository.postlike.dto.JoinedPostWithArea;
import org.springframework.data.domain.Slice;

public record LikedPostFindResponseList(Slice<LikedPostFindResponse> likedPosts) {

    public static LikedPostFindResponseList from(Slice<JoinedPostWithArea> joinedWithAreas) {
        Slice<LikedPostFindResponse> mapResponse = joinedWithAreas.map(e -> new LikedPostFindResponse(
                e.getPost().getTitle(),
                e.getPost().getPostImageList().get(0).getUrl(),
                e.getArea().getName(),
                e.getPost().getPrice(),
                e.getPost().getLikeCount()));

        return new LikedPostFindResponseList(mapResponse);
    }

}
