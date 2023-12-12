package com.example.dictionary.application.validator;

import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.domain.service.WordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.dictionary.utils.TestUtils.DEFINITION_NOT_FOUND;
import static com.example.dictionary.utils.TestUtils.DUPLICATE_WORD;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_DTO;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_NOT_CONTAINS_TEST;
import static com.example.dictionary.utils.TestUtils.TEST;
import static com.example.dictionary.utils.TestUtils.TEST_DEFINITION_DTO;
import static com.example.dictionary.utils.TestUtils.TEST_DTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordValidatorTest {

    @Mock
    private WordService wordService;

    @InjectMocks
    private WordValidator wordValidator;

    @Test
    void testValidate_whenFieldsAreValid_nothingIsThrown() {
        TEST_DTO.addDefinition(TEST_DEFINITION_DTO);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> wordValidator.validate(TEST_DTO));
        verify(wordService).getWordByName(anyString());
    }

    @Test
    void testValidate_whenWordIsPresentInDatabase_thenThrow() {
        TEST_DTO.addDefinition(TEST_DEFINITION_DTO);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.of(TEST));

        DuplicateResourceException duplicateResourceException = assertThrows(
                DuplicateResourceException.class,
                () -> wordValidator.validate(TEST_DTO));

        assertEquals(DUPLICATE_WORD, duplicateResourceException.getMessage());
        verify(wordService).getWordByName(anyString());
    }

    @Test
    void testValidate_whenWordDoesNotHaveDefinition_thenThrow() {
        when(wordService.getWordByName(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordValidator.validate(TEST_DTO));

        assertEquals(DEFINITION_NOT_FOUND, resourceNotFoundException.getMessage());
        verify(wordService).getWordByName(anyString());
    }

    @Test
    void testValidate_whenProvidingExampleWithoutWord_thenThrow() {
        TEST_DTO.addExample(EXAMPLE_DTO);
        TEST_DTO.addDefinition(TEST_DEFINITION_DTO);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordValidator.validate(TEST_DTO));

        assertEquals(EXAMPLE_NOT_CONTAINS_TEST, resourceNotFoundException.getMessage());
        verify(wordService).getWordByName(anyString());
    }
}