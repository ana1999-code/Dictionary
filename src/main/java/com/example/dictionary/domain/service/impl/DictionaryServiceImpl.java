package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Dictionary;
import com.example.dictionary.domain.repository.DictionaryRepository;
import com.example.dictionary.domain.service.DictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public List<Dictionary> getAllDictionaries() {
        return dictionaryRepository.findAll();
    }

    @Override
    public void addDictionary(Dictionary dictionary) {
        dictionaryRepository.save(dictionary);
    }

    @Override
    public Optional<Dictionary> getDictionaryByName(String name) {
        return dictionaryRepository.findDictionaryByName(name);
    }

    @Override
    public boolean existsDictionaryByName(String name) {
        return dictionaryRepository.existsByName(name);
    }

    @Override
    public boolean existsDictionaryByUrl(String url) {
        return dictionaryRepository.existsByUrl(url);
    }
}
