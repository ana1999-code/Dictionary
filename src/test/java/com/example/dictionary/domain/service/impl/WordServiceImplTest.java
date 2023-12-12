package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.dictionary.utils.TestUtils.TEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordServiceImplTest {

    @InjectMocks
    private WordServiceImpl wordService;

    @Mock
    private WordRepository wordRepository;

    @Test
    void testGetAllWords() {
        List<Word> expectedList = List.of(TEST);
        when(wordRepository.findAll()).thenReturn(expectedList);

        List<Word> actualList = wordService.getAllWords();

        assertEquals(expectedList, actualList);
        verify(wordRepository).findAll();
    }

    @Test
    void testGetWordByName_whenProvideValidName_thenReturnTheWord() {
        when(wordRepository.findByName(anyString())).thenReturn(Optional.of(TEST));

        Optional<Word> actualWord = wordService.getWordByName(TEST.getName());

        assertNotNull(actualWord);
        assertEquals(TEST, actualWord.get());
        verify(wordRepository).findByName(anyString());
    }

    @Test
    void testGetWordByName_whenProvideInvalidName_thenReturnEmpty() {
        when(wordRepository.findByName(anyString())).thenReturn(Optional.empty());

        Optional<Word> actualWord = wordService.getWordByName(TEST.getName());

        assertTrue(actualWord.isEmpty());
        verify(wordRepository).findByName(anyString());
    }
}