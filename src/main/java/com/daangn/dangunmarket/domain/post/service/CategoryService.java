package com.daangn.dangunmarket.domain.post.service;

import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.repository.post.CategoryRepository;
import com.daangn.dangunmarket.global.Exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_FOUND_CATEGORY_ENTITY;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_CATEGORY_ENTITY));
    }

}
