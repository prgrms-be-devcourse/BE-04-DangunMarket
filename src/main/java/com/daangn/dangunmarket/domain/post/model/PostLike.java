package com.daangn.dangunmarket.domain.post.model;

import com.daangn.dangunmarket.global.entity.BaseTimeEntity;
import io.jsonwebtoken.lang.Assert;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private Long memberId;

    @Builder
    public PostLike(Post post, Long memberId) {
        Assert.notNull(memberId, "memberId는 null일 수 없습니다.");
        Assert.notNull(post, "post는 null일 수 없습니다.");

        this.post = post;
        this.memberId = memberId;
    }

}
