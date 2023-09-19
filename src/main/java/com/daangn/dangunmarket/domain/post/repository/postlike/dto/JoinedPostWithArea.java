package com.daangn.dangunmarket.domain.post.repository.postlike.dto;

import com.daangn.dangunmarket.domain.area.model.Area;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.querydsl.core.annotations.QueryProjection;

public record JoinedPostWithArea(Post post, Area area) {

    @QueryProjection
    public JoinedPostWithArea(Post post, Area area) {
        this.post = post;
        this.area = area;
    }
}