package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostLikeApiResponse;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;
import org.springframework.stereotype.Component;

@Component
public class PostApiMapper {

    public PostCreateRequestParam toPostCreateRequest(PostCreateApiRequest request) {
        return new PostCreateRequestParam(
                request.memberId(),
                request.areaId(),
                request.latitude(),
                request.longitude(),
                request.alias(),
                request.files(),
                request.categoryId(),
                request.title(),
                request.content(),
                request.price(),
                request.isOfferAllowed(),
                request.refreshedAt());
    }

    public PostLikeApiResponse toPostLikeApiResponse(PostLikeResponse response) {
        return PostLikeApiResponse.of(
                response.likeCount(),
                response.isLiked()
        );
    }

}
