package com.daangn.dangunmarket.domain.post.model;

import com.daangn.dangunmarket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;


@Getter
@Entity
@Table(name = "post_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", referencedColumnName = "id")
    private Post post;

    public PostImage(String url) {
        Assert.notNull(url, "url은 null값이 들어올 수 없습니다.");

        this.url = url;
    }

    public void changePost(Post post) {
        if (this.post != null) {
            this.post.getPostImageList().remove(this);
        }
        this.post = post;
    }

    public void removePost() {
        if (this.post != null) {
            this.post = null;
        }
    }

}
