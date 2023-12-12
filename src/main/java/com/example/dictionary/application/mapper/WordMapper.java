package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.domain.entity.Word;
import org.mapstruct.Mapper;

@Mapper
public interface WordMapper {

    Word wordDtoToWord(WordDto wordDto);

    WordDto wordToWordDto(Word word);
}
