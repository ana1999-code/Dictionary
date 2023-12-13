package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Category;
import com.example.dictionary.domain.repository.CategoryRepository;
import com.example.dictionary.domain.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.getCategoryByName(name);
    }
}
