package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.mapper.WordMapper;
import com.example.dictionary.application.validator.WordValidator;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.service.CategoryService;
import com.example.dictionary.domain.service.DefinitionService;
import com.example.dictionary.domain.service.WordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.dictionary.utils.TestUtils.WORD;
import static com.example.dictionary.utils.TestUtils.WORD_DTO;
import static com.example.dictionary.utils.TestUtils.WORD_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    private CategoryService categoryService;

    @Mock
    private DefinitionService definitionService;

    @Mock
    private WordMapper wordMapper;

    @Mock
    private WordValidator wordValidator;

    @Test
    void testGetAllWords() {
        when(wordService.getAllWords()).thenReturn(List.of(WORD));
        when(wordMapper.wordToWordDto(any(Word.class))).thenReturn(WORD_DTO);

        List<WordDto> actualWords = wordFacade.getAllWords();

        assertTrue(actualWords.contains(WORD_DTO));
        verify(wordService).getAllWords();
        verify(wordMapper).wordToWordDto(any(Word.class));
    }

    @Test
    void testGetWordByName_whenGetWordByValidName_thenReturnTheWordDto() {
        when(wordService.getWordByName(anyString())).thenReturn(Optional.of(WORD));
        when(wordMapper.wordToWordDto(any(Word.class))).thenReturn(WORD_DTO);

        WordDto actualWordDto = wordFacade.getWordByName(WORD.getName());

        assertEquals(WORD_DTO, actualWordDto);
        verify(wordService).getWordByName(anyString());
        verify(wordMapper).wordToWordDto(any(Word.class));
    }

    @Test
    void testGetWordByName_whenGetWordByInvalidName_thenThrow() {
        when(wordService.getWordByName(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordFacade.getWordByName(WORD.getName())
        );

        assertEquals(WORD_NOT_FOUND, resourceNotFoundException.getMessage());
        verify(wordService).getWordByName(anyString());
        verify(wordMapper, times(0)).wordToWordDto(any(Word.class));
    }

    @Test
    void testAddWord() {
        doNothing().when(wordValidator).validate(WORD_DTO);
        when(wordMapper.wordDtoToWord(any(WordDto.class))).thenReturn(WORD);
        when(wordService.addWord(WORD)).thenReturn(Optional.of(WORD));
        when(wordMapper.wordToWordDto(any(Word.class))).thenReturn(WORD_DTO);
        when(categoryService.getCategoryByName(anyString())).thenReturn(Optional.of(WORD.getCategory()));

        WORD.getDefinitions().forEach(definition ->
                when(definitionService.getDefinitionByText(definition.getText()))
                        .thenReturn(Optional.of(definition))
        );

        WordDto actualWord = wordFacade.addWord(WORD_DTO);

        assertEquals(WORD_DTO, actualWord);
        verify(wordValidator).validate(WORD_DTO);
        verify(wordService).addWord(WORD);
        verify(wordMapper).wordToWordDto(any(Word.class));
        verify(wordMapper).wordDtoToWord(any(WordDto.class));
    }

    @Test
    void testDeleteByName_whenWordExists_thenIsDeleted() {
        when(wordService.getWordByName(anyString())).thenReturn(Optional.of(WORD));
        doNothing().when(wordService).deleteWordByName(anyString());

        assertDoesNotThrow(() -> wordFacade.deleteWordByName(WORD.getName()));
        verify(wordService).getWordByName(anyString());
        verify(wordService).deleteWordByName(anyString());
    }

    @Test
    void testDeleteByName_whenWordDoesNotExists_thenThrow() {
        when(wordService.getWordByName(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> wordFacade.deleteWordByName(WORD.getName()));

        assertEquals(WORD_NOT_FOUND, resourceNotFoundException.getMessage());
    }
}