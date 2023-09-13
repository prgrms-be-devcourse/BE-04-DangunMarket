package com.daangn.dangunmarket.domain.post.repository.dto;

import com.daangn.dangunmarket.domain.post.model.Post;
import com.querydsl.core.annotations.QueryProjection;


public record PostDto(Post post,
                      String areaName) {

    @QueryProjection
    public PostDto(Post post, String areaName) {
        this.post = post;
        this.areaName = areaName;
    }

}
