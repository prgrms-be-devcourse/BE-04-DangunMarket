package com.daangn.dangunmarket.domain.post.model;

import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import com.daangn.dangunmarket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long areaId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private LocationPreference localPreference;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostImage> postImageList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Category category;

    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    @Embedded
    @Column(nullable = false)
    private Title title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Price price;

    @Column(nullable = false)
    private boolean isOfferAllowed;

    @Column(updatable = false, name = "refreshed_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime refreshedAt;

    @Column(nullable = false)
    private Integer likeCount;

    @Builder
    public Post(Long memberId, Long areaId, LocationPreference localPreference, List<PostImage> postImageList, Category category, TradeStatus tradeStatus, Title title, String content, Price price, boolean isOfferAllowed, LocalDateTime refreshedAt, Integer likeCount) {
        Assert.notNull(memberId, "memberId는 null값이 들어올 수 없습니다.");
        Assert.notNull(areaId, "areaId는 null값이 들어올 수 없습니다.");
        Assert.notNull(tradeStatus, "tradeStatus는 null값이 들어올 수 없습니다.");
        Assert.notNull(title, "title은 null값이 들어올 수 없습니다.");
        Assert.notNull(content, "content는 null값이 들어올 수 없습니다.");
        Assert.notNull(likeCount, "likeCount는 null값이 들어올 수 없습니다.");
        Assert.notNull(postImageList, "postImageList는 null값이 들어올 수 없습니다.");

        this.memberId = memberId;
        this.areaId = areaId;
        this.localPreference = localPreference;
        this.postImageList = postImageList;
        this.category = category;
        this.tradeStatus = tradeStatus;
        this.title = title;
        this.content = content;
        this.price = price;
        this.isOfferAllowed = isOfferAllowed;
        this.refreshedAt = refreshedAt;
        this.likeCount = likeCount;
    }

    public List<PostImage> getPostImageList() {
        return postImageList;
    }

    public void addPostImage(PostImage postImage) {
        this.postImageList.add(postImage);
        postImage.changePost(this);
    }

    public String getTitle() {
        return title.getTitle();
    }

    public long getPrice() {
        return price.getValue();
    }

    public void like() {
        likeCount += 1;
    }

    public void cancelLike() {
        likeCount -= 1;
    }

    public void changeTradeStatus(TradeStatus tradeStatus) {
        Assert.notNull(tradeStatus, "tradeStatus는 null이 될 수 없습니다.");

        this.tradeStatus = tradeStatus;
    }
}
