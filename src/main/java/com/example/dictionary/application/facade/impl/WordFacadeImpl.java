package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.application.mapper.WordMapper;
import com.example.dictionary.application.validator.WordValidator;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.service.WordService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WordFacadeImpl implements WordFacade {

    private final WordService wordService;

    private final WordMapper wordMapper;

    private final WordValidator validator;

    public WordFacadeImpl(WordService wordService, WordMapper wordMapper, WordValidator validator) {
        this.wordService = wordService;
        this.wordMapper = wordMapper;
        this.validator = validator;
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

    @Override
    public WordDto addWord(WordDto wordDto) {
        validator.validate(wordDto);

        Word word = wordMapper.wordDtoToWord(wordDto);
        Word addedWord = wordService.addWord(word)
                .orElseThrow(IllegalStateException::new);

        return wordMapper.wordToWordDto(addedWord);
    }
}
