package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Category;
import com.example.dictionary.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.dictionary.utils.TestUtils.CATEGORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void testGetCategoryByName() {
        when(categoryRepository.getCategoryByName(anyString())).thenReturn(Optional.of(CATEGORY));

        Optional<Category> categoryByName = categoryService.getCategoryByName(CATEGORY.getName());

        assertEquals(CATEGORY, categoryByName.get());
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(CATEGORY));

        List<Category> categories = categoryService.getAllCategories();

        assertTrue(categories.contains(CATEGORY));
    }
}