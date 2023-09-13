package com.daangn.dangunmarket.domain.post.controller.mapper;

import com.daangn.dangunmarket.domain.post.controller.dto.PostToUpdateApiResponse;
import com.daangn.dangunmarket.domain.post.facade.dto.PostToUpdateResponseParam;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PostUpdateApiMapper {

    PostToUpdateApiResponse toPostToUpdateApiResponse(PostToUpdateResponseParam postToUpdateResponseParam);
}
