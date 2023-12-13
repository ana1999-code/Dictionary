package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.Category;

import java.util.Optional;

public interface CategoryService {

    Optional<Category> getCategoryByName(String name);
}
