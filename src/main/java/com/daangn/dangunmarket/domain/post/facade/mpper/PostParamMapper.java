package com.daangn.dangunmarket.domain.post.facade.mpper;

import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostsGetResponseParam;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponses;
import com.daangn.dangunmarket.domain.post.service.dto.PostUpdateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PostParamMapper {

    PostCreateRequest toPostCreateRequest(
            PostCreateRequestParam requestParam,
            LocationPreference locationPreference,
            List<PostImage> postImages,
            Category category);

    @Mapping(source = "areaName", target = "userAreaName")
    PostsGetResponseParam toPostsGetResponseParam(String areaName, PostGetResponses responsePage);

    PostUpdateRequest toPostUpdateRequest(
            PostUpdateRequestParam request,
            LocationPreference locationPreference,
            List<PostImage> postImages,
            Category category,
            Long areaId);

}
