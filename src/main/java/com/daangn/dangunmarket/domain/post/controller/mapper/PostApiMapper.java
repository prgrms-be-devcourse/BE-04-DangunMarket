package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.PostUpdateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostGetApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostSearchApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostSearchApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostToUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostUpdateStatusApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.postlike.PostLikeApiResponse;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostGetResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;

import com.daangn.dangunmarket.domain.post.service.dto.PostUpdateStatusRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PostApiMapper {

    PostCreateRequestParam toPostCreateRequestParam(PostCreateApiRequest request, List<MultipartFile> files, Long memberId);

    PostLikeApiResponse toPostLikeApiResponse(PostLikeResponse response);

    PostUpdateStatusRequest toPostUpdateStatusRequest(PostUpdateStatusApiRequest request, Long postId);

    PostGetApiResponses toPostGetApiResponses(PostGetResponseParams responseParam);

    PostSearchRequestParam toPostSearchRequestParam(PostSearchApiRequest request, Pageable pageable);

    PostSearchApiResponses toPostSearchApiResponses(PostSearchResponseParams responseParams);

    PostToUpdateApiResponse toPostToUpdateApiResponse(PostToUpdateResponseParam postToUpdateResponseParam);

    PostUpdateRequestParam toPostUpdateRequestParam(PostUpdateApiRequest postUpdateApiRequest);

    PostUpdateApiResponse toPostUpdateApiResponse(Long postId);

}
