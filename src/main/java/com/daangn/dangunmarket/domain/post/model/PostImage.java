package com.daangn.dangunmarket.domain.post.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "post_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    public PostImage(String url) {
        Assert.notNull(url, "url은 null값이 들어올 수 없습니다.");

        this.url = url;
    }

    public void changeProduct(Post post) {
        if (this.post != null) {
            this.post.getPostImageList().remove(this);
        }
        this.post = post;
    }

}
