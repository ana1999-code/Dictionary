package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.domain.entity.Word;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.dictionary.utils.TestUtils.TEST;
import static com.example.dictionary.utils.TestUtils.TEST_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WordMapperTest {

    private final WordMapper wordMapper = Mappers.getMapper(WordMapper.class);

    @Test
    void testWordDtoToWordMapper() {
        Word word = wordMapper.wordDtoToWord(TEST_DTO);

        assertEquals(TEST_DTO.getName(), word.getName());
        assertEquals(TEST_DTO.getCategory().getName(), word.getCategory().getName());
    }

    @Test
    void testWordToWordDtoMapper() {
        WordDto wordDto = wordMapper.wordToWordDto(TEST);

        assertEquals(TEST.getName(), wordDto.getName());
        assertEquals(TEST.getCategory().getName(), wordDto.getCategory().getName());
    }
}