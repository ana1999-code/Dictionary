package com.example.dictionary.rest.controller;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface WordController {

    ResponseEntity<List<WordDto>> getAllWords();

    ResponseEntity<WordDto> getWordByName(String name);

    ResponseEntity<WordDto> addWord(WordDto wordDto);

    void deleteWordByName(String name);

    ResponseEntity<WordDto> addDefinitionToWord(String name, DefinitionDto definitionDto);

    ResponseEntity<WordDto> removeDefinitionFromWord(String name, DefinitionDto definitionDto);

    ResponseEntity<WordDto> addExampleToWord(String name, ExampleDto exampleDto);

    ResponseEntity<WordDto> removeExampleFromWord(String name, ExampleDto exampleDto);

    ResponseEntity<Set<WordDto>> getAllSynonyms(String name);

    ResponseEntity<Set<WordDto>> getAllAntonyms(String name);

    void addSynonym(String name, WordDto synonym);

    void removeSynonym(String name, WordDto synonym);

    void addAntonym(String name, WordDto antonym);

    void removeAntonym(String name, WordDto antonym);

    void addComment(String name, CommentDto commentDto);

    void removeComment(String name, CommentDto commentDto);
}
