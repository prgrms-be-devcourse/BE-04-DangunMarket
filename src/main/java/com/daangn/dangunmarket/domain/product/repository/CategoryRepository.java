package com.daangn.dangunmarket.domain.product.repository;

import com.daangn.dangunmarket.domain.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
