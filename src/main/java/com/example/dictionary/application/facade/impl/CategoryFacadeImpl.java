package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.facade.CategoryFacade;
import com.example.dictionary.application.mapper.CategoryMapper;
import com.example.dictionary.domain.entity.Category;
import com.example.dictionary.domain.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryFacadeImpl implements CategoryFacade {

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    public CategoryFacadeImpl(CategoryService categoryService,
                              CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return categories.stream()
                .map(categoryMapper::categoryToCategoryDto)
                .toList();
    }
}
