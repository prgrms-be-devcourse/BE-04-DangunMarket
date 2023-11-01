package com.daangn.dangunmarket.domain.post.controller.dto.postlike;

import com.daangn.dangunmarket.domain.post.service.dto.LikedPostFindResponseList;
import org.springframework.data.domain.Slice;

public record LikedPostFindApiResponseList(Slice<LikedPostFindApiResponse> responses) {

    public static LikedPostFindApiResponseList from(LikedPostFindResponseList responseList){
        Slice<LikedPostFindApiResponse> mapResponses = responseList.likedPosts()
                .map(e -> new LikedPostFindApiResponse(
                        e.title(),
                        e.imageUrl(),
                        e.areaName(),
                        e.price(),
                        e.likeCount()));

        return new LikedPostFindApiResponseList(mapResponses);
    }

}
