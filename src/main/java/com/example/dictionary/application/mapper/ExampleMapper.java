package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.domain.entity.Example;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ExampleMapper {

    @Mapping(target = "words", ignore = true)
    Example exampleDtoToExample(ExampleDto exampleDto);

    ExampleDto exampleToExampleDto(Example example);
}
