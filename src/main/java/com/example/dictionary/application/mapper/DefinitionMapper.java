package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.domain.entity.Definition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DefinitionMapper {

    DefinitionDto definitionToDefinitionDto(Definition definition);

    @Mapping(target = "words", ignore = true)
    Definition definitionDtoToDefinition(DefinitionDto definitionDto);
}
