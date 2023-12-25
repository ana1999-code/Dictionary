package com.example.dictionary.application.facade;

import com.example.dictionary.application.dto.CategoryDto;

import java.util.List;

public interface CategoryFacade {

    List<CategoryDto> getAllCategories();
}
