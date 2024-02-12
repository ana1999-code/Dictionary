package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.DictionaryDto;
import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.entity.Dictionary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

@Mapper
public interface DefinitionMapper {

    @Mapping(target = "dictionaries", source = "dictionaries", qualifiedByName = "dictionariesToDictionariesDtoDetails")
    DefinitionDto definitionToDefinitionDto(Definition definition);

    @Mapping(target = "words", ignore = true)
    Definition definitionDtoToDefinition(DefinitionDto definitionDto);

    @Named("dictionariesToDictionariesDtoDetails")
    static Set<DictionaryDto> dictionariesToDictionariesDtoDetails(Set<Dictionary> dictionaries) {
        Set<DictionaryDto> dictionaryDtos = new HashSet<>();
        dictionaries.forEach(
                dictionary -> {
                    DictionaryDto dictionaryDto = new DictionaryDto();
                    dictionaryDto.setName(dictionary.getName());
                    dictionaryDto.setUrl(dictionary.getUrl());
                    dictionaryDtos.add(dictionaryDto);
                }
        );

        return dictionaryDtos;
    }
}
