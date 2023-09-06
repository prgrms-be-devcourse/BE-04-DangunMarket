package com.daangn.dangunmarket.domain.category.service;

import com.daangn.dangunmarket.domain.category.domain.Category;
import com.daangn.dangunmarket.domain.category.domain.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 카테고리는 존재하지 않습니다.")); // illegalㅇㅣ 맞는가?
    }

}
