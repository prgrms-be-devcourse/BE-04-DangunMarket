package com.daangn.dangunmarket.domain.product.model;

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
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long areaId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private LocationPreference localPreference;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ProductImage> productImageList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Category category;

    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private boolean isOfferAllowed;

    @Column(updatable = false, name = "refreshed_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime refreshedAt;

    @Builder
    public Product(Long memberId, Long areaId, LocationPreference localPreference, List<ProductImage> productImageList, Category category, TradeStatus tradeStatus, String title, String content, Long price, boolean isOfferAllowed, LocalDateTime refreshedAt) {
        Assert.notNull(memberId, "memberId는 null값이 들어올 수 없습니다.");
        Assert.notNull(areaId, "areaId는 null값이 들어올 수 없습니다.");
        Assert.notNull(tradeStatus, "tradeStatus는 null값이 들어올 수 없습니다.");
        Assert.notNull(title, "title는 null값이 들어올 수 없습니다.");
        Assert.notNull(content, "content는 null값이 들어올 수 없습니다.");
        Assert.notNull(price, "price는 null값이 들어올 수 없습니다.");
        Assert.notNull(isOfferAllowed, "isOfferAllowed는 null값이 들어올 수 없습니다.");

        this.memberId = memberId;
        this.areaId = areaId;
        this.localPreference = localPreference;
        this.productImageList = productImageList;
        this.category = category;
        this.tradeStatus = tradeStatus;
        this.title = title;
        this.content = content;
        this.price = price;
        this.isOfferAllowed = isOfferAllowed;
        this.refreshedAt = refreshedAt;
    }

    public List<ProductImage> getProductImageList() {
        return productImageList;
    }

    public void addProductImage(ProductImage productImage) {
        this.productImageList.add(productImage);
        productImage.changeProduct(this);
    }

}
