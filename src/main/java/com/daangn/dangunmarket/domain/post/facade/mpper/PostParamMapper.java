package com.daangn.dangunmarket.domain.post.facade.mpper;

import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostGetResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchResponseParams;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponses;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchConditionRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchResponses;
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
    PostGetResponseParams toPostsGetResponseParam(String areaName, PostGetResponses responsePage);

    PostSearchConditionRequest toPostSearchConditionRequest(PostSearchRequestParam postSearchRequestParam);

    @Mapping(source = "areaName", target = "userAreaName")
    PostSearchResponseParams toPostSearchResponseParams(String areaName, PostSearchResponses postSearchResponses);
}
