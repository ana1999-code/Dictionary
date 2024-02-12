package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.Dictionary;

import java.util.List;
import java.util.Optional;

public interface DictionaryService {

    List<Dictionary> getAllDictionaries();

    void addDictionary(Dictionary dictionary);

    Optional<Dictionary> getDictionaryByName(String name);

    boolean existsDictionaryByName(String name);

    boolean existsDictionaryByUrl(String url);
}
