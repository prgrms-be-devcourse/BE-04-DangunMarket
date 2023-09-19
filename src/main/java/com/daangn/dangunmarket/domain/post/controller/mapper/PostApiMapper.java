package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.*;
import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostGetApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.PostLikeApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostSearchApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostSearchApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.PostToUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostGetResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PostApiMapper {

    PostCreateRequestParam toPostCreateRequestParam(PostCreateApiRequest request, Long memberId);

    PostLikeApiResponse toPostLikeApiResponse(PostLikeResponse response);

    PostGetApiResponses toPostGetApiResponses(PostGetResponseParams responseParam);

    PostSearchRequestParam toPostSearchRequestParam(PostSearchApiRequest request, Pageable pageable);

    PostSearchApiResponses toPostSearchApiResponses(PostSearchResponseParams responseParams);

    PostToUpdateApiResponse toPostToUpdateApiResponse(PostToUpdateResponseParam postToUpdateResponseParam);

    @Mapping(target = "files", source = "request", qualifiedByName = "mapFiles")
    @Mapping(target = "urls", source = "request", qualifiedByName = "mapUrls")
    PostUpdateRequestParam toPostUpdateRequestParam(PostUpdateApiRequest request);

    @Named("mapFiles")
    static List<MultipartFile> mapFiles(PostUpdateApiRequest request) {
        if (request != null && request.getFiles() != null) {
            return request.postImageUpdateApiRequest().files();
        } else {
            return Collections.emptyList();
        }
    }

    @Named("mapUrls")
    static List<String> mapUrls(PostUpdateApiRequest request) {
        if (request != null && request.getUrls() != null) {
            return request.postImageUpdateApiRequest().urls();
        } else {
            return Collections.emptyList();
        }
    }
    PostUpdateApiResponse toPostUpdateApiResponse(Long postId);

}
