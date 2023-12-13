package com.example.dictionary.utils;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.domain.entity.Category;
import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.entity.Example;
import com.example.dictionary.domain.entity.Word;

public class TestUtils {

    public static final CategoryDto CATEGORY_DTO = new CategoryDto("test_category");

    public static final Category CATEGORY = new Category("test_category");

    public static final WordDto WORD_DTO = new WordDto("test", CATEGORY_DTO);

    public static final Word WORD = new Word("test", CATEGORY);

    public static final String WORD_NOT_FOUND = "Word %s not found".formatted(WORD.getName());

    public static final DefinitionDto DEFINITION_DTO = new DefinitionDto("Test definition");

    public static final String DUPLICATE_WORD = "Word %s already exists".formatted(WORD.getName());

    public static final String DEFINITION_NOT_FOUND = "Word %s should have at least one definition".formatted(WORD.getName());

    public static final ExampleDto EXAMPLE_DTO = new ExampleDto("Example without word");

    public static final String EXAMPLE_NOT_CONTAINS_TEST = "Provided example does not contain the word %s".formatted(WORD.getName());

    public static final String EXAMPLE_WITHOUT_WORD = "Provided example does not contain the word %s".formatted(WORD.getName());

    public static final Example EXAMPLE = new Example("Test example");

    public static final Definition DEFINITION = new Definition("Definition test");
}
