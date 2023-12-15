package com.example.dictionary.application.facade;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.domain.entity.Category;
import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.entity.Example;

import java.util.List;

public interface WordFacade {

    List<WordDto> getAllWords();

    WordDto getWordByName(String name);

    WordDto addWord(WordDto wordDto);

    void deleteWordByName(String name);

    List<CategoryDto> getAllCategories();

    List<DefinitionDto> getAllDefinitions();

    List<ExampleDto> getAllExamples();
}
