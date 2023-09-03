package com.daangn.dangunmarket.domain.product.productImage.domain;

import com.daangn.dangunmarket.domain.product.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "products_id", referencedColumnName = "id", nullable = false)
    private Product product;

}
