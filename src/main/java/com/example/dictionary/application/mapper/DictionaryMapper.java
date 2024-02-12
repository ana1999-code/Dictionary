package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.DictionaryDto;
import com.example.dictionary.domain.entity.Dictionary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DictionaryMapper {

    Dictionary dictionaryDtoToDictionary(DictionaryDto dictionaryDto);

    @Mapping(target = "words", ignore = true)
    @Mapping(target = "definitions", ignore = true)
    DictionaryDto dictionaryToDictionaryDto(Dictionary dictionary);
}
