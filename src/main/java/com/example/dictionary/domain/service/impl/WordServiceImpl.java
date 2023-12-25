package com.example.dictionary.domain.service.impl;

import com.example.dictionary.application.annotation.ContributeByUser;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.repository.WordRepository;
import com.example.dictionary.domain.service.WordService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    @Transactional
    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }

    @Override
    public Optional<Word> getWordByName(String name) {
        return wordRepository.findByName(name);
    }

    @Override
    @ContributeByUser
    public Word addWord(Word word) {
        return wordRepository.save(word);
    }

    @Override
    public boolean existsWordByName(String name) {
        return wordRepository.existsByName(name);
    }

    @Override
    public void deleteWordByName(String name) {
        wordRepository.deleteByName(name);
    }
}
