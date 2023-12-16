package com.example.dictionary.rest.controller.impl;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.rest.controller.WordController;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.get.words}")
@Profile("rest")
public class WordControllerImpl implements WordController {

    private final WordFacade wordFacade;

    public WordControllerImpl(WordFacade wordFacade) {
        this.wordFacade = wordFacade;
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public ResponseEntity<List<WordDto>> getAllWords() {
        List<WordDto> words = wordFacade.getAllWords();
        return new ResponseEntity<>(words, OK);
    }

    @Override
    @GetMapping("{name}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public ResponseEntity<WordDto> getWordByName(@PathVariable("name") String name) {
        WordDto word = wordFacade.getWordByName(name);
        return new ResponseEntity<>(word, OK);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('word:write')")
    public ResponseEntity<WordDto> addWord(@RequestBody @Valid WordDto wordDto) {
        WordDto addedWord = wordFacade.addWord(wordDto);
        return new ResponseEntity<>(addedWord, CREATED);
    }

    @Override
    @DeleteMapping("{name}")
    @ResponseStatus(value = NO_CONTENT)
    @PreAuthorize("hasAuthority('word:write')")
    public void deleteWordByName(@PathVariable("name") String name) {
        wordFacade.deleteWordByName(name);
    }

    @Override
    @GetMapping("${api.get.categories}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = wordFacade.getAllCategories();
        return new ResponseEntity<>(categories, OK);
    }
}
