package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.mapper.WordMapper;
import com.example.dictionary.application.validator.WordValidator;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.service.WordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.dictionary.utils.TestUtils.TEST;
import static com.example.dictionary.utils.TestUtils.TEST_DTO;
import static com.example.dictionary.utils.TestUtils.WORD_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordFacadeImplTest {

    @InjectMocks
    private WordFacadeImpl wordFacade;

    @Mock
    private WordService wordService;

    @Mock
    private WordMapper wordMapper;

    @Mock
    private WordValidator wordValidator;

    @Test
    void testGetAllWords() {
        when(wordService.getAllWords()).thenReturn(List.of(TEST));
        when(wordMapper.wordToWordDto(any(Word.class))).thenReturn(TEST_DTO);

        List<WordDto> actualWords = wordFacade.getAllWords();

        assertTrue(actualWords.contains(TEST_DTO));
        verify(wordService).getAllWords();
        verify(wordMapper).wordToWordDto(any(Word.class));
    }

    @Test
    void testGetWordByName_whenGetWordByValidName_thenReturnTheWordDto() {
        when(wordService.getWordByName(anyString())).thenReturn(Optional.of(TEST));
        when(wordMapper.wordToWordDto(any(Word.class))).thenReturn(TEST_DTO);

        WordDto actualWordDto = wordFacade.getWordByName(TEST.getName());

        assertEquals(TEST_DTO, actualWordDto);
        verify(wordService).getWordByName(anyString());
        verify(wordMapper).wordToWordDto(any(Word.class));
    }

    @Test
    void testGetWordByName_whenGetWordByInvalidName_thenThrow() {
        when(wordService.getWordByName(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordFacade.getWordByName(TEST.getName())
        );

        assertEquals(WORD_NOT_FOUND, resourceNotFoundException.getMessage());
        verify(wordService).getWordByName(anyString());
        verify(wordMapper, times(0)).wordToWordDto(any(Word.class));
    }

    @Test
    void testAddWord() {
        doNothing().when(wordValidator).validate(TEST_DTO);
        when(wordMapper.wordDtoToWord(any(WordDto.class))).thenReturn(TEST);
        when(wordService.addWord(TEST)).thenReturn(Optional.of(TEST));
        when(wordMapper.wordToWordDto(any(Word.class))).thenReturn(TEST_DTO);

        WordDto actualWord = wordFacade.addWord(TEST_DTO);

        assertEquals(TEST_DTO, actualWord);
        verify(wordValidator).validate(TEST_DTO);
        verify(wordService).addWord(TEST);
        verify(wordMapper).wordToWordDto(any(Word.class));
        verify(wordMapper).wordDtoToWord(any(WordDto.class));
    }
}