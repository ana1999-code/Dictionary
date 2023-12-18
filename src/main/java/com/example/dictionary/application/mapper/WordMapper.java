package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.domain.entity.Word;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WordMapper {

    @Mapping(target = "synonyms", ignore = true)
    @Mapping(target = "antonyms", ignore = true)
    Word wordDtoToWord(WordDto wordDto);

    @Mapping(target = "synonyms", ignore = true)
    @Mapping(target = "antonyms", ignore = true)
    WordDto wordToWordDto(Word word);
}
