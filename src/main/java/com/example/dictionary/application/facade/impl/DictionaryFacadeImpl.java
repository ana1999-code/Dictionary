package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.DictionaryDto;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.DictionaryFacade;
import com.example.dictionary.application.mapper.DictionaryMapper;
import com.example.dictionary.application.validator.DictionaryValidator;
import com.example.dictionary.domain.entity.Dictionary;
import com.example.dictionary.domain.service.DictionaryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DictionaryFacadeImpl implements DictionaryFacade {

    private final DictionaryService dictionaryService;

    private final DictionaryMapper dictionaryMapper;

    private final DictionaryValidator dictionaryValidator;

    public DictionaryFacadeImpl(DictionaryService dictionaryService,
                                DictionaryMapper dictionaryMapper,
                                DictionaryValidator dictionaryValidator) {
        this.dictionaryService = dictionaryService;
        this.dictionaryMapper = dictionaryMapper;
        this.dictionaryValidator = dictionaryValidator;
    }

    @Override
    public List<DictionaryDto> getAllDictionaries() {
        List<Dictionary> allDictionaries = dictionaryService.getAllDictionaries();
        return allDictionaries.stream()
                .map(dictionaryMapper::dictionaryToDictionaryDto)
                .toList();
    }

    @Override
    public void addDictionary(DictionaryDto dictionaryDto) {
        dictionaryValidator.validate(dictionaryDto);

        Dictionary dictionary = dictionaryMapper.dictionaryDtoToDictionary(dictionaryDto);
        dictionaryService.addDictionary(dictionary);
    }

    @Override
    public DictionaryDto getDictionaryByName(String name) {
        Dictionary dictionary = dictionaryService.getDictionaryByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(""));

        return dictionaryMapper.dictionaryToDictionaryDto(dictionary);
    }
}
