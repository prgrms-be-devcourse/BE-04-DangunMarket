package com.daangn.dangunmarket.domain.post.repository.category;

import com.daangn.dangunmarket.domain.post.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
