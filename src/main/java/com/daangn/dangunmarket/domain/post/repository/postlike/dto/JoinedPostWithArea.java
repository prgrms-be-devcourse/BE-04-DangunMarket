package com.daangn.dangunmarket.domain.post.repository.postlike.dto;

import com.daangn.dangunmarket.domain.area.model.Area;
import com.daangn.dangunmarket.domain.post.model.Post;
import lombok.Getter;

@Getter
public class JoinedPostWithArea {

    private Post post;
    private Area area;

    public JoinedPostWithArea() {
    }

    public JoinedPostWithArea(Post post, Area area) {
        this.post = post;
        this.area = area;
    }
}
