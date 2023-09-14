package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.*;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostsGetResponseParam;
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PostApiMapper {

    PostCreateRequestParam toPostCreateRequestParam(PostCreateApiRequest request, Long memberId);

    PostLikeApiResponse toPostLikeApiResponse(PostLikeResponse response);

    PostGetApiResponses toPostGetApiResponses(PostsGetResponseParam responseParam);

    PostToUpdateApiResponse toPostToUpdateApiResponse(PostToUpdateResponseParam postToUpdateResponseParam);

    PostUpdateRequestParam toPostUpdateRequestParam(PostUpdateApiRequest postUpdateApiRequest);

    PostUpdateApiResponse toPostUpdateApiResponse(Long postId);

}
