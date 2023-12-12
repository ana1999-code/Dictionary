package com.example.dictionary.utils;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.domain.entity.Category;
import com.example.dictionary.domain.entity.Word;

public class TestUtils {

    public static final CategoryDto TEST_CATEGORY_DTO = new CategoryDto("test_category");

    public static final Category TEST_CATEGORY = new Category("test_category");

    public static final WordDto TEST_DTO = new WordDto("test", TEST_CATEGORY_DTO);

    public static final Word TEST = new Word("test", TEST_CATEGORY);

    public static final String WORD_NOT_FOUND = "Word %s not found".formatted(TEST.getName());
}
