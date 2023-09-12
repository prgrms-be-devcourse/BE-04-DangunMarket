package com.daangn.dangunmarket.domain.post.facade.mpper;

import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostsGetResponseParam;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

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

    @Mapping(source = "responsePage.content", target = "contents")
    @Mapping(source = "areaName", target = "userAreaName")
    @Mapping(source = "responsePage.number", target = "page")
    public PostsGetResponseParam toPostsGetResponseParam(String areaName, Page<PostGetResponse> responsePage);

}
