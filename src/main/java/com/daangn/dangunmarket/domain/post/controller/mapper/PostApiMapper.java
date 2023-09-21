package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.PostDeleteApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostUpdateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostGetApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostSearchApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostSearchApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.post.PostToUpdateApiResponse;
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
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
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

    @Mapping(target = "files", source = "request", qualifiedByName = "mapFiles")
    @Mapping(target = "urls", source = "request", qualifiedByName = "mapUrls")
    PostUpdateRequestParam toPostUpdateRequestParam(PostUpdateApiRequest request);

    PostDeleteApiResponse toPostDeleteApiResponse(Long deletedPostId);


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
