package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.domain.entity.Word;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.dictionary.utils.TestUtils.WORD;
import static com.example.dictionary.utils.TestUtils.WORD_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WordMapperTest {

    private final WordMapper wordMapper = Mappers.getMapper(WordMapper.class);

    @Test
    void testWordDtoToWordMapper() {
        Word word = wordMapper.wordDtoToWord(WORD_DTO);

        assertEquals(WORD_DTO.getName(), word.getName());
        assertEquals(WORD_DTO.getCategory().getName(), word.getCategory().getName());
    }

    @Test
    void testWordToWordDtoMapper() {
        WordDto wordDto = wordMapper.wordToWordDto(WORD);

        assertEquals(WORD.getName(), wordDto.getName());
        assertEquals(WORD.getCategory().getName(), wordDto.getCategory().getName());
    }
}