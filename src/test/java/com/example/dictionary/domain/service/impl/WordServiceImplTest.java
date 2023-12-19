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

import static com.example.dictionary.utils.TestUtils.WORD;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
        List<Word> expectedList = List.of(WORD);
        when(wordRepository.findAll()).thenReturn(expectedList);

        List<Word> actualList = wordService.getAllWords();

        assertEquals(expectedList, actualList);
        verify(wordRepository).findAll();
    }

    @Test
    void testGetWordByName_whenProvideValidName_thenReturnTheWord() {
        when(wordRepository.findByName(anyString())).thenReturn(Optional.of(WORD));

        Optional<Word> actualWord = wordService.getWordByName(WORD.getName());

        assertNotNull(actualWord);
        assertEquals(WORD, actualWord.get());
        verify(wordRepository).findByName(anyString());
    }

    @Test
    void testGetWordByName_whenProvideInvalidName_thenReturnEmpty() {
        when(wordRepository.findByName(anyString())).thenReturn(Optional.empty());

        Optional<Word> actualWord = wordService.getWordByName(WORD.getName());

        assertTrue(actualWord.isEmpty());
        verify(wordRepository).findByName(anyString());
    }

    @Test
    void testAddWord() {
        when(wordRepository.save(any(Word.class))).thenReturn(WORD);

        Word addedWord = wordService.addWord(WORD);

        assertEquals(WORD, addedWord);
        verify(wordRepository).save(any(Word.class));
    }

    @Test
    void testExistsByName_whenWordExists_thenReturnTrue() {
        when(wordRepository.existsByName(anyString())).thenReturn(true);

        boolean existsWordByName = wordService.existsWordByName(WORD.getName());

        assertTrue(existsWordByName);
        verify(wordRepository).existsByName(anyString());
    }

    @Test
    void testExistsByName_whenWordDoesNotExist_thenReturnFalse() {
        when(wordRepository.existsByName(anyString())).thenReturn(false);

        boolean existsWordByName = wordService.existsWordByName(WORD.getName());

        assertFalse(existsWordByName);
        verify(wordRepository).existsByName(anyString());
    }

    @Test
    void testDeleteByName() {
        doNothing().when(wordRepository).deleteByName(anyString());

        assertDoesNotThrow(() -> wordService.deleteWordByName(WORD.getName()));
        verify(wordRepository).deleteByName(anyString());
    }
}