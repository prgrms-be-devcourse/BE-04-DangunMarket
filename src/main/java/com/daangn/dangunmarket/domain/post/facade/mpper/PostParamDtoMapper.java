package com.daangn.dangunmarket.domain.post.facade.mpper;

import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.service.dto.PostToUpdateResponse;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PostParamDtoMapper {

    @Mapping(target = "latitude", source = "postToUpdateResponse.locationPreference.latitude")
    @Mapping(target = "longitude", source = "postToUpdateResponse.locationPreference.longitude")
    @Mapping(target = "locationPreferenceAlias", source = "postToUpdateResponse.locationPreference.alias")
    @Mapping(target = "categoryId", source = "postToUpdateResponse.category.id")
    @Mapping(target = "categoryName", source = "postToUpdateResponse.category.name")
    @Mapping(target = "postImages", source = "postToUpdateResponse.postImages", qualifiedByName = "mapPostImages")
    PostToUpdateResponseParam toPostToUpdateResponseParam(PostToUpdateResponse postToUpdateResponse);

    @Named("mapPostImages")
    static List<String> mapPostImages(List<PostImage> postImages) {
        return postImages.stream()
                .map(PostImage::getUrl)
                .toList();
    }

}
