package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.post.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.postlike.PostLikeApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostUpdateStatusApiRequest;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;
import com.daangn.dangunmarket.domain.post.service.dto.PostUpdateStatusRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PostApiMapper {

    PostCreateRequestParam toPostCreateRequestParam(PostCreateApiRequest request, Long memberId);

    PostLikeApiResponse toPostLikeApiResponse(PostLikeResponse response);

    PostUpdateStatusRequest toPostUpdateStatusRequest(PostUpdateStatusApiRequest request, Long postId);
}
