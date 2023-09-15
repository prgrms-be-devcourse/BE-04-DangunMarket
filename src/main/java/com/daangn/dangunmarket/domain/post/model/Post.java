package com.daangn.dangunmarket.domain.post.model;

import com.daangn.dangunmarket.domain.post.exception.TooEarlyToRefreshException;
import com.daangn.dangunmarket.domain.post.model.vo.PostImages;
import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import com.daangn.dangunmarket.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    private static final int AVAILABLE_REFRESH_DAYS = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long areaId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private LocationPreference localPreference;

    @Embedded
    private PostImages postImages = new PostImages();

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

    @Column(name = "refreshed_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime refreshedAt;

    @Column(nullable = false)
    private Integer likeCount;

    @Builder
    public Post(Long memberId, Long areaId, LocationPreference localPreference, List<PostImage> postImages, Category category, TradeStatus tradeStatus, Title title, String content, Price price, boolean isOfferAllowed, LocalDateTime refreshedAt, Integer likeCount) {
        Assert.notNull(memberId, "memberId는 null값이 들어올 수 없습니다.");
        Assert.notNull(areaId, "areaId는 null값이 들어올 수 없습니다.");
        Assert.notNull(tradeStatus, "tradeStatus는 null값이 들어올 수 없습니다.");
        Assert.notNull(title, "title은 null값이 들어올 수 없습니다.");
        Assert.notNull(content, "content는 null값이 들어올 수 없습니다.");
        Assert.notNull(likeCount, "likeCount는 null값이 들어올 수 없습니다.");
        Assert.notNull(postImages, "postImages는 null값이 들어올 수 없습니다.(postImage가 0이면 빈 List라도 넣어주세요.)");

        postImages.forEach(this::addPostImage);
        this.memberId = memberId;
        this.areaId = areaId;
        this.localPreference = localPreference;
        this.category = category;
        this.tradeStatus = tradeStatus;
        this.title = title;
        this.content = content;
        this.price = price;
        this.isOfferAllowed = isOfferAllowed;
        this.refreshedAt = refreshedAt;
        this.likeCount = likeCount;
    }

    public List<PostImage> getPostImages() {
        return postImages.getPostImageList();
    }

    public void addPostImage(PostImage postImage) {
        postImages.addPostImage(postImage);
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

    public boolean isNotOwner(Long memberId){
        return !Objects.equals(this.memberId, memberId);
    }

    public void changeRefreshedAt(LocalDateTime refreshedAt) {
        Assert.notNull(refreshedAt, "refreshedAt은 null이 될 수 없습니다.");

        checkRefreshedAt(refreshedAt);
        this.refreshedAt = refreshedAt;
    }

    private void checkRefreshedAt(LocalDateTime inputTime) {
        Duration betweenTime = Duration.between(refreshedAt, inputTime);

        if (betweenTime.toDays() < AVAILABLE_REFRESH_DAYS) {
            Duration availableRefreshDays = Duration.ofDays(AVAILABLE_REFRESH_DAYS);

            throw new TooEarlyToRefreshException(
                    availableRefreshDays.minus(betweenTime)
            );
        }
    }

}
