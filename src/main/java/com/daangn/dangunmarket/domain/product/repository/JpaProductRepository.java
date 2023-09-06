package com.daangn.dangunmarket.domain.product.repository;

import com.daangn.dangunmarket.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long> {
}
