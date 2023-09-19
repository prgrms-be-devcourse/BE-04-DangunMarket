package com.daangn.dangunmarket.domain.post.model;

import com.daangn.dangunmarket.domain.post.exception.TooEarlyToRefreshException;
import com.daangn.dangunmarket.domain.post.model.vo.PostEditor;
import com.daangn.dangunmarket.domain.post.model.vo.PostImages;
import com.daangn.dangunmarket.domain.post.model.vo.Price;
import com.daangn.dangunmarket.domain.post.model.vo.Title;
import com.daangn.dangunmarket.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private LocationPreference locationPreference;

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
    public Post(Long memberId, Long areaId, LocationPreference locationPreference, List<PostImage> postImages, Category category, TradeStatus tradeStatus, Title title, String content, Price price, boolean isOfferAllowed, LocalDateTime refreshedAt, Integer likeCount) {
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
        this.locationPreference = locationPreference;
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
        this.postImages.addPostImage(postImage);
        postImage.changePost(this);
    }

    //이름 , 주석 // update 용으로 하나 만들고 중복같지만 목적이 다른
    public void addPostImages(List<PostImage> postImages) {
        for (PostImage postImage : postImages) {
            this.addPostImage(postImage);
            postImage.changePost(this);
        }
    }

    public void removePostImage(PostImage postImage) {
        postImages.removePostImage(postImage);
        postImage.changeIsDeleted(true);
        postImage.removePost();
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

    public boolean isNotOwner(Long memberId) {
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

    public void deletePost() {
        postImages.getPostImageList()
                .stream()
                .forEach(PostImage::deletePostImage); //이미지 상태 변경
        if (locationPreference != null) {
            locationPreference.deleteLocationPreference(); //선호 장소 상태 변경
        }
        isDeleted = true; //post 상태 변경
    }

    public void edit(PostEditor postEditor) {
        areaId = postEditor.areaId();
        locationPreference = postEditor.locationPreference();
        category = postEditor.category();
        title = new Title(postEditor.title());
        content = postEditor.content();
        price = new Price(postEditor.price());
        isOfferAllowed = postEditor.isOfferAllowed();

        this.addPostImages(postEditor.postImages());
    }

    public boolean isCreatedBy(Long userId ){
        if (!Objects.equals(this.memberId, userId)) {
            return false;
        }

        return true;
    }
}
