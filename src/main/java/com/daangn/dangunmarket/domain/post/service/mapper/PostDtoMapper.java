package com.daangn.dangunmarket.domain.post.service.mapper;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import com.daangn.dangunmarket.domain.post.service.dto.PostToUpdateResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PostDtoMapper {

    @Mapping(source = "post.id", target = "postId")
    PostToUpdateResponse toPostToUpdateResponse(Post post);

}


