package com.example.dictionary.application.validator;

import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.IllegalOperationException;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.domain.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.dictionary.utils.TestUtils.DEFINITION_DTO;
import static com.example.dictionary.utils.TestUtils.DEFINITION_NOT_FOUND_FOR_WORD;
import static com.example.dictionary.utils.TestUtils.DUPLICATE_WORD;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_DTO;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_NOT_CONTAINS_TEST;
import static com.example.dictionary.utils.TestUtils.WORD;
import static com.example.dictionary.utils.TestUtils.WORD_DTO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordValidatorTest {

    @Mock
    private WordService wordService;

    @InjectMocks
    private WordValidator wordValidator;

    @BeforeEach
    void setUp() {
        WORD_DTO.getExamples().clear();
        WORD_DTO.addDefinition(DEFINITION_DTO);
    }

    @Test
    void testValidate_whenFieldsAreValid_nothingIsThrown() {
        WORD_DTO.addDefinition(DEFINITION_DTO);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> wordValidator.validate(WORD_DTO));
        verify(wordService).getWordByName(anyString());
    }

    @Test
    void testValidate_whenWordIsPresentInDatabase_thenThrow() {
        WORD_DTO.addDefinition(DEFINITION_DTO);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.of(WORD));

        DuplicateResourceException duplicateResourceException = assertThrows(
                DuplicateResourceException.class,
                () -> wordValidator.validate(WORD_DTO));

        assertEquals(DUPLICATE_WORD, duplicateResourceException.getMessage());
        verify(wordService).getWordByName(anyString());
    }

    @Test
    void testValidate_whenWordDoesNotHaveDefinition_thenThrow() {
        WORD_DTO.getDefinitions().clear();

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordValidator.validate(WORD_DTO));

        assertEquals(DEFINITION_NOT_FOUND_FOR_WORD, resourceNotFoundException.getMessage());
        verify(wordService).getWordByName(anyString());
    }

    @Test
    void testValidate_whenProvidingExampleWithoutWord_thenThrow() {
        WORD_DTO.addExample(EXAMPLE_DTO);
        WORD_DTO.addDefinition(DEFINITION_DTO);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.empty());

        IllegalOperationException illegalOperationException = assertThrows(
                IllegalOperationException.class,
                () -> wordValidator.validate(WORD_DTO));

        assertEquals(EXAMPLE_NOT_CONTAINS_TEST, illegalOperationException.getMessage());
        verify(wordService).getWordByName(anyString());
    }
}