package com.daangn.dangunmarket.domain.product.model;

import com.daangn.dangunmarket.domain.product.model.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wish_lists")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id", referencedColumnName = "id", nullable = false)
    private Product product;

}