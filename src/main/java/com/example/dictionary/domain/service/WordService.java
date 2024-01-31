package com.example.dictionary.domain.service;

import com.example.dictionary.application.report.data.WordDetail;
import com.example.dictionary.domain.entity.Word;

import java.util.List;
import java.util.Optional;

public interface WordService {

    List<Word> getAllWords();

    List<Word> getAllWords(int page, int pageSize);

    Optional<Word> getWordByName(String name);

    Word addWord(Word word);

    boolean existsWordByName(String name);

    void deleteWordByName(String name);

    List<WordDetail> getWordsDetails();

    Optional<Word> getWordByNameWithContributors(String name);
}
