package com.daangn.dangunmarket.domain.post.repository.postlike.dto;

import com.daangn.dangunmarket.domain.area.model.Area;
import com.daangn.dangunmarket.domain.post.model.Post;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinedWithArea{

    private Post post;
    private Area area;

    public JoinedWithArea() {
    }

    public JoinedWithArea(Post post, Area area) {
        this.post = post;
        this.area = area;
    }
}
