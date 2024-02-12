package com.example.dictionary.application.facade;

import com.example.dictionary.application.dto.DictionaryDto;

import java.util.List;

public interface DictionaryFacade {

    List<DictionaryDto> getAllDictionaries();

    void addDictionary(DictionaryDto dictionaryDto);

    DictionaryDto getDictionaryByName(String name);
}
