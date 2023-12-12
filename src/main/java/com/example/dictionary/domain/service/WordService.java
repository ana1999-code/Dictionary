package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.Word;

import java.util.List;
import java.util.Optional;

public interface WordService {

    List<Word> getAllWords();

    Optional<Word> getWordByName(String name);
}
