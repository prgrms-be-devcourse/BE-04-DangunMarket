package com.daangn.dangunmarket.domain.product.model;

import com.daangn.dangunmarket.domain.product.model.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Table(name = "product_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id", referencedColumnName = "id")
    private Product product;

    public ProductImage(String url) {
        Assert.notNull(url, "url은 null값이 들어올 수 없습니다.");

        this.url = url;
    }

    public void changeProduct(Product product) {
        if (this.product != null) {
            this.product.getProductImageList().remove(this);
        }
        this.product = product;
    }

}
