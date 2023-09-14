package com.daangn.dangunmarket.domain.post.facade.mpper;

import com.daangn.dangunmarket.domain.post.facade.dto.PostCreateRequestParam;
<<<<<<< HEAD
import com.daangn.dangunmarket.domain.post.facade.dto.PostUpdateRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostsGetResponseParam;
=======
import com.daangn.dangunmarket.domain.post.facade.dto.PostGetResponseParams;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchRequestParam;
import com.daangn.dangunmarket.domain.post.facade.dto.PostSearchResponseParams;
>>>>>>> 3e76499b5ecc0d8775fbdc41488435743af76c10
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.LocationPreference;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.dto.PostCreateRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostGetResponses;
<<<<<<< HEAD
import com.daangn.dangunmarket.domain.post.service.dto.PostUpdateRequest;
=======
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchConditionRequest;
import com.daangn.dangunmarket.domain.post.service.dto.PostSearchResponses;
>>>>>>> 3e76499b5ecc0d8775fbdc41488435743af76c10
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

<<<<<<< HEAD
    PostUpdateRequest toPostUpdateRequest(
            PostUpdateRequestParam request,
            LocationPreference locationPreference,
            List<PostImage> postImages,
            Category category,
            Long areaId);

=======
    PostSearchConditionRequest toPostSearchConditionRequest(PostSearchRequestParam postSearchRequestParam);

    @Mapping(source = "areaName", target = "userAreaName")
    PostSearchResponseParams toPostSearchResponseParams(String areaName, PostSearchResponses postSearchResponses);
>>>>>>> 3e76499b5ecc0d8775fbdc41488435743af76c10
}
