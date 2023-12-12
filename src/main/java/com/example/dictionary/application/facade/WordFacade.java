package com.example.dictionary.application.facade;

import com.example.dictionary.application.dto.WordDto;

import java.util.List;

public interface WordFacade {

    List<WordDto> getAllWords();

    WordDto getWordByName(String name);
}
