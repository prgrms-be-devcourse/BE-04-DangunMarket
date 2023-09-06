package com.daangn.dangunmarket.domain.product.repository;

import com.daangn.dangunmarket.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
