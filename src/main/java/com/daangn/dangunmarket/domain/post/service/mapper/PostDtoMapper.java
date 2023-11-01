package com.daangn.dangunmarket.domain.post.service.mapper;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.service.dto.PostToUpdateResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PostDtoMapper {
    default PostToUpdateResponse toPostToUpdateResponse(Post post) {
        return new PostToUpdateResponse(
                post.getId(),
                post.getLocationPreference(),
                post.getPostImages(),
                post.getCategory(),
                post.getTradeStatus(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.isOfferAllowed()
        );
    }
}


