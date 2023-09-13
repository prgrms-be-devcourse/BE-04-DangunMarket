package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostGetApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.PostLikeApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostToUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
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

}
