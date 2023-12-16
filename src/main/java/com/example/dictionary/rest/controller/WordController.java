package com.example.dictionary.rest.controller;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.WordDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WordController {

    ResponseEntity<List<WordDto>> getAllWords();

    ResponseEntity<WordDto> getWordByName(String name);

    ResponseEntity<WordDto> addWord(WordDto wordDto);

    void deleteWordByName(String name);

    ResponseEntity<List<CategoryDto>> getAllCategories();

    void addDefinitionToWord(String name, DefinitionDto definitionDto);

    void removeDefinitionFromWord(String name, DefinitionDto definitionDto);
}
