package com.daangn.dangunmarket.domain.post.model.vo;

import com.daangn.dangunmarket.domain.post.model.PostImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor
public class PostImages {
    private static final int MAX_IMAGE_COUNT = 3;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostImage> postImageList = new ArrayList<>();

    public void addPostImage(PostImage postImage) {
        Assert.notNull(postImage, "postImage는 null이 들어올 수 없습니다.");
        if (this.postImageList.size() >= MAX_IMAGE_COUNT ){
            throw new IllegalArgumentException("이미지는 4개 이상 들어올 수 없습니다.");
        }

        this.postImageList.add(postImage);
    }

}
