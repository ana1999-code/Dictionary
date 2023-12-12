package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.application.mapper.WordMapper;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.service.WordService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WordFacadeImpl implements WordFacade {

    private final WordService wordService;

    private final WordMapper wordMapper;

    public WordFacadeImpl(WordService wordService, WordMapper wordMapper) {
        this.wordService = wordService;
        this.wordMapper = wordMapper;
    }

    @Override
    public List<WordDto> getAllWords() {
        List<Word> allWords = wordService.getAllWords();
        return allWords.stream()
                .map(wordMapper::wordToWordDto)
                .toList();
    }

    @Override
    public WordDto getWordByName(String name) {
        Word word = wordService.getWordByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Word %s not found".formatted(name)));

        return wordMapper.wordToWordDto(word);
    }
}
