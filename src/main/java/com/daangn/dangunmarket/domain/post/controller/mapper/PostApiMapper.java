package com.daangn.dangunmarket.domain.post.controller.mapper;

<<<<<<< HEAD
import com.daangn.dangunmarket.domain.post.controller.dto.*;
=======
import com.daangn.dangunmarket.domain.post.controller.dto.PostCreateApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostGetApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.PostLikeApiResponse;
import com.daangn.dangunmarket.domain.post.controller.dto.PostSearchApiRequest;
import com.daangn.dangunmarket.domain.post.controller.dto.PostSearchApiResponses;
import com.daangn.dangunmarket.domain.post.controller.dto.PostToUpdateApiResponse;
>>>>>>> 3e76499b5ecc0d8775fbdc41488435743af76c10
import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostGetResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
<<<<<<< HEAD
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostsGetResponseParam;
=======

>>>>>>> 3e76499b5ecc0d8775fbdc41488435743af76c10
import com.daangn.dangunmarket.domain.post.service.dto.PostLikeResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;

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

    PostUpdateRequestParam toPostUpdateRequestParam(PostUpdateApiRequest postUpdateApiRequest);

    PostUpdateApiResponse toPostUpdateApiResponse(Long postId);

}
