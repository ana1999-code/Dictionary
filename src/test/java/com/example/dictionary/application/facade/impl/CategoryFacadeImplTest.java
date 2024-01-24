package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.mapper.CategoryMapper;
import com.example.dictionary.domain.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.dictionary.utils.TestUtils.CATEGORY;
import static com.example.dictionary.utils.TestUtils.CATEGORY_DTO;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryFacadeImplTest {

    @InjectMocks
    private CategoryFacadeImpl categoryFacade;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @Test
    void testGetAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(List.of(CATEGORY));
        when(categoryMapper.categoryToCategoryDto(any())).thenReturn(CATEGORY_DTO);

        List<CategoryDto> actualResult = categoryFacade.getAllCategories();

        assertTrue(actualResult.contains(CATEGORY_DTO));
        verify(categoryService).getAllCategories();
        verify(categoryMapper).categoryToCategoryDto(any());
    }
}