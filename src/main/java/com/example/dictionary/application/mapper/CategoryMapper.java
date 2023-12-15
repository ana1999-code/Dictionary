package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CategoryMapper {

    @Mapping(target = "words", ignore = true)
    Category categoryDtoToCategory(CategoryDto categoryDto);

    CategoryDto categoryToCategoryDto(Category category);
}
