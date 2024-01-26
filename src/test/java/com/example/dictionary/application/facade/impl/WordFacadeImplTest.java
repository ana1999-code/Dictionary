package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.IllegalOperationException;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.mapper.CommentMapper;
import com.example.dictionary.application.mapper.DefinitionMapper;
import com.example.dictionary.application.mapper.ExampleMapper;
import com.example.dictionary.application.mapper.WordMapper;
import com.example.dictionary.application.security.util.SecurityUtils;
import com.example.dictionary.application.util.WordEntityAssociationUtil;
import com.example.dictionary.application.validator.example.ExampleValidator;
import com.example.dictionary.application.validator.WordValidator;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.service.CategoryService;
import com.example.dictionary.domain.service.CommentService;
import com.example.dictionary.domain.service.DefinitionService;
import com.example.dictionary.domain.service.ExampleService;
import com.example.dictionary.domain.service.UserService;
import com.example.dictionary.domain.service.WordService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.dictionary.utils.TestUtils.ANTONYM;
import static com.example.dictionary.utils.TestUtils.ANTONYM_DTO;
import static com.example.dictionary.utils.TestUtils.COMMENT;
import static com.example.dictionary.utils.TestUtils.COMMENT_DTO;
import static com.example.dictionary.utils.TestUtils.COMMENT_NOT_FOUND;
import static com.example.dictionary.utils.TestUtils.DEFINITION;
import static com.example.dictionary.utils.TestUtils.DEFINITION_DTO;
import static com.example.dictionary.utils.TestUtils.DEFINITION_IS_PRESENT;
import static com.example.dictionary.utils.TestUtils.DEFINITION_NOT_FOUND;
import static com.example.dictionary.utils.TestUtils.DEFINITION_NOT_FOUND_FOR_THE_WORD;
import static com.example.dictionary.utils.TestUtils.EXAMPLE;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_ALREADY_PRESENT;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_DTO;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_DTO_WITHOUT_WORD;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_NOT_CONTAINS_WORD;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_NOT_FOUND;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_NOT_FOUND_FOR_THE_WORD;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_WITHOUT_WORD;
import static com.example.dictionary.utils.TestUtils.EXISTING_DEFINITION_DTO_FOR_WORD;
import static com.example.dictionary.utils.TestUtils.EXISTING_DEFINITION_FOR_WORD;
import static com.example.dictionary.utils.TestUtils.NON_EXISTING_DEFINITION_DTO_FOR_WORD;
import static com.example.dictionary.utils.TestUtils.NON_EXISTING_DEFINITION_FOR_WORD;
import static com.example.dictionary.utils.TestUtils.ONLY_ONE_DEFINITION;
import static com.example.dictionary.utils.TestUtils.SYNONYM;
import static com.example.dictionary.utils.TestUtils.SYNONYM_DTO;
import static com.example.dictionary.utils.TestUtils.USER;
import static com.example.dictionary.utils.TestUtils.WORD;
import static com.example.dictionary.utils.TestUtils.WORD_DTO;
import static com.example.dictionary.utils.TestUtils.WORD_IS_ALREADY_LINKED;
import static com.example.dictionary.utils.TestUtils.WORD_IS_NOT_LINKED;
import static com.example.dictionary.utils.TestUtils.WORD_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
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
    private ExampleService exampleService;

    @Mock
    private CommentService commentService;

    @Mock
    private UserService userService;

    @Mock
    private WordMapper wordMapper;

    @Mock
    private WordValidator wordValidator;

    @Mock
    private DefinitionMapper definitionMapper;

    @Mock
    private ExampleMapper exampleMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private WordEntityAssociationUtil associationUtil;

    @Mock
    private MessageSource messageSource;

    @Captor
    private ArgumentCaptor<Word> wordArgumentCaptor;

    @BeforeEach
    void setUp() {
        WORD.getDefinitions().clear();
        WORD.addDefinition(EXISTING_DEFINITION_FOR_WORD);
        WORD.getExamples().clear();
        WORD.getSynonyms().clear();
        WORD.getAntonyms().clear();

        WORD_DTO.getDefinitions().clear();
        WORD_DTO.addDefinition(EXISTING_DEFINITION_DTO_FOR_WORD);
        WORD_DTO.getExamples().clear();
    }

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
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(WORD_NOT_FOUND);
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
        WORD.addExample(EXAMPLE);
        WORD_DTO.addExample(EXAMPLE_DTO);

        doNothing().when(wordValidator).validate(WORD_DTO);
        when(wordMapper.wordDtoToWord(any(WordDto.class))).thenReturn(WORD);
        when(wordService.addWord(WORD)).thenReturn(WORD);
        when(wordMapper.wordToWordDto(any(Word.class))).thenReturn(WORD_DTO);
        doNothing().when(associationUtil).associateWordWithEntities(any());

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
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(WORD_NOT_FOUND);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> wordFacade.deleteWordByName(WORD.getName()));

        assertEquals(WORD_NOT_FOUND, resourceNotFoundException.getMessage());
    }

    @Test
    void testAddDefinitionToWord_whenAddNewDefinition_thenReturnWordWithDefinition() {
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(definitionService.getDefinitionByText(anyString())).thenReturn(Optional.empty());
        when(definitionMapper.definitionDtoToDefinition(any(DefinitionDto.class))).thenReturn(DEFINITION);
        when(wordService.addWord(any(Word.class))).thenReturn(WORD);
        when(wordMapper.wordToWordDto(any(Word.class))).thenReturn(WORD_DTO);

        wordFacade.addDefinitionToWord(WORD_DTO.getName(), DEFINITION_DTO);

        verify(wordService).addWord(wordArgumentCaptor.capture());
        Word capturedWord = wordArgumentCaptor.getValue();
        assertTrue(capturedWord.getDefinitions().contains(DEFINITION));
    }

    @Test
    void testAddDefinitionToWord_whenAddExistingDefinitionInDatabase_thenReturnWordWithDefinition() {
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(definitionService.getDefinitionByText(anyString())).thenReturn(Optional.of(DEFINITION));
        when(wordService.addWord(any(Word.class))).thenReturn(WORD);
        when(wordMapper.wordToWordDto(any(Word.class))).thenReturn(WORD_DTO);

        wordFacade.addDefinitionToWord(WORD_DTO.getName(), DEFINITION_DTO);

        verify(wordService).addWord(wordArgumentCaptor.capture());
        Word capturedWord = wordArgumentCaptor.getValue();
        assertTrue(capturedWord.getDefinitions().contains(DEFINITION));
    }

    @Test
    void testAddDefinitionToWord_whenAddExistingDefinitionInWord_thenThrow() {
        try (MockedStatic<WordFacadeImpl> wordFacadeMocked = mockStatic(WordFacadeImpl.class)) {
            wordFacadeMocked.when(() -> WordFacadeImpl.verifyDefinitionIsNotPresent(any(), any(), any()))
                    .thenThrow(
                            new DuplicateResourceException(
                                    DEFINITION_IS_PRESENT
                            )
                    );
            WORD_DTO.addDefinition(DEFINITION_DTO);
            WORD.addDefinition(DEFINITION);

            when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
            when(definitionService.getDefinitionByText(anyString())).thenReturn(Optional.of(DEFINITION));

            assertThrows(
                    DuplicateResourceException.class,
                    () -> wordFacade.addDefinitionToWord(WORD.getName(), DEFINITION_DTO)
            );
        }
    }

    @Test
    void testRemoveDefinition_whenRemoveExistingDefinition_thenReturnWordWithoutDefinition() {
        WORD.addDefinition(DEFINITION);
        WORD_DTO.addDefinition(DEFINITION_DTO);

        when(definitionMapper.definitionDtoToDefinition(any())).thenReturn(DEFINITION);
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(definitionService.getDefinitionByText(anyString())).thenReturn(Optional.of(DEFINITION));
        when(wordService.addWord(any())).thenReturn(WORD);
        when(wordMapper.wordToWordDto(any())).thenReturn(WORD_DTO);

        wordFacade.removeDefinitionFromWord(WORD_DTO.getName(), DEFINITION_DTO);

        verify(wordService).addWord(wordArgumentCaptor.capture());
        Word capturedWord = wordArgumentCaptor.getValue();

        assertFalse(capturedWord.getDefinitions().contains(DEFINITION));
    }

    @Test
    void testRemoveDefinition_whenRemoveNonExistingDefinition_thenThrow() {
        when(definitionMapper.definitionDtoToDefinition(any())).thenReturn(DEFINITION);
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(definitionService.getDefinitionByText(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(DEFINITION_NOT_FOUND);

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordFacade.removeDefinitionFromWord(WORD_DTO.getName(), DEFINITION_DTO)
        );

        assertEquals(DEFINITION_NOT_FOUND, resourceNotFoundException.getMessage());
    }

    @Test
    void testRemoveDefinition_whenRemoveTheOnlyPresentDefinition_thenThrow() {
        when(definitionMapper.definitionDtoToDefinition(any())).thenReturn(EXISTING_DEFINITION_FOR_WORD);
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(definitionService.getDefinitionByText(anyString())).thenReturn(Optional.of(EXISTING_DEFINITION_FOR_WORD));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(ONLY_ONE_DEFINITION);

        IllegalOperationException illegalOperationException = assertThrows(
                IllegalOperationException.class,
                () -> wordFacade.removeDefinitionFromWord(WORD_DTO.getName(), EXISTING_DEFINITION_DTO_FOR_WORD)
        );

        assertEquals(ONLY_ONE_DEFINITION,
                illegalOperationException.getMessage());
    }

    @Test
    void testRemoveDefinition_whenRemoveDefinitionNotFoundForWord_thenThrow() {
        WORD.addDefinition(DEFINITION);
        WORD_DTO.addDefinition(DEFINITION_DTO);

        when(definitionMapper.definitionDtoToDefinition(any())).thenReturn(NON_EXISTING_DEFINITION_FOR_WORD);
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(definitionService.getDefinitionByText(anyString())).thenReturn(Optional.of(NON_EXISTING_DEFINITION_FOR_WORD));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(DEFINITION_NOT_FOUND_FOR_THE_WORD);

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordFacade.removeDefinitionFromWord(WORD_DTO.getName(), NON_EXISTING_DEFINITION_DTO_FOR_WORD)
        );

        assertEquals(DEFINITION_NOT_FOUND_FOR_THE_WORD,
                resourceNotFoundException.getMessage());
    }

    @Test
    void testAddExampleToWord_whenAddNewExample_thenExampleIsAdded() {
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(exampleService.getExampleByText(anyString())).thenReturn(Optional.empty());
        when(exampleMapper.exampleDtoToExample(any())).thenReturn(EXAMPLE);
        when(wordService.addWord(any())).thenReturn(WORD);
        when(wordMapper.wordToWordDto(any())).thenReturn(WORD_DTO);

        wordFacade.addExampleToWord(WORD.getName(), EXAMPLE_DTO);

        verify(wordService).addWord(wordArgumentCaptor.capture());
        Word updatedWord = wordArgumentCaptor.getValue();

        assertTrue(updatedWord.getExamples().contains(EXAMPLE));
    }

    @Test
    void testAddExampleToWord_whenAddExistingExampleInDb_thenExampleIsAdded() {
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(exampleService.getExampleByText(anyString())).thenReturn(Optional.of(EXAMPLE));
        when(wordService.addWord(any())).thenReturn(WORD);
        when(wordMapper.wordToWordDto(any())).thenReturn(WORD_DTO);

        wordFacade.addExampleToWord(WORD.getName(), EXAMPLE_DTO);

        verify(wordService).addWord(wordArgumentCaptor.capture());
        Word updatedWord = wordArgumentCaptor.getValue();

        assertTrue(updatedWord.getExamples().contains(EXAMPLE));
    }

    @Test
    void testAddExampleToWord_whenAddExistingExampleInWord_thenThrow() {
        WORD.addExample(EXAMPLE);
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(exampleService.getExampleByText(anyString())).thenReturn(Optional.of(EXAMPLE));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(EXAMPLE_ALREADY_PRESENT);

        DuplicateResourceException duplicateResourceException = assertThrows(
                DuplicateResourceException.class,
                () -> wordFacade.addExampleToWord(WORD.getName(), EXAMPLE_DTO)
        );

        assertEquals(EXAMPLE_ALREADY_PRESENT, duplicateResourceException.getMessage());
    }

    @Test
    void testAddExampleToWord_whenAddExampleWithoutWord_thenThrow() {
        try (MockedStatic<ExampleValidator> exampleValidatorMocked = mockStatic(ExampleValidator.class)) {
            exampleValidatorMocked.when(() -> ExampleValidator
                            .validate(WORD.getName(), EXAMPLE_WITHOUT_WORD.getText()))
                    .thenThrow(new IllegalOperationException(EXAMPLE_NOT_CONTAINS_WORD));

            when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
            when(exampleService.getExampleByText(anyString())).thenReturn(Optional.empty());
            when(exampleMapper.exampleDtoToExample(any())).thenReturn(EXAMPLE_WITHOUT_WORD);

            IllegalOperationException illegalOperationException = assertThrows(
                    IllegalOperationException.class,
                    () -> wordFacade.addExampleToWord(WORD.getName(), EXAMPLE_DTO_WITHOUT_WORD)
            );

            assertEquals(EXAMPLE_NOT_CONTAINS_WORD, illegalOperationException.getMessage());
        }
    }

    @Test
    void testRemoveExample_whenRemoveExample_thenReturnWordWithoutExample() {
        WORD.addExample(EXAMPLE);
        WORD_DTO.addExample(EXAMPLE_DTO);

        when(exampleMapper.exampleDtoToExample(any())).thenReturn(EXAMPLE);
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(exampleService.getExampleByText(anyString())).thenReturn(Optional.of(EXAMPLE));
        when(wordService.addWord(any())).thenReturn(WORD);
        when(wordMapper.wordToWordDto(any())).thenReturn(WORD_DTO);

        wordFacade.removeExampleFromWord(WORD.getName(), EXAMPLE_DTO);

        verify(wordService).addWord(wordArgumentCaptor.capture());
        Word capturedWord = wordArgumentCaptor.getValue();
        assertFalse(capturedWord.getExamples().contains(EXAMPLE));
    }

    @Test
    void testRemoveExample_whenRemoveNonExistingExample_thenThrow() {
        when(exampleMapper.exampleDtoToExample(any())).thenReturn(EXAMPLE);
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(exampleService.getExampleByText(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(EXAMPLE_NOT_FOUND);

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordFacade.removeExampleFromWord(WORD.getName(), EXAMPLE_DTO)
        );

        assertEquals(EXAMPLE_NOT_FOUND, resourceNotFoundException.getMessage());
    }

    @Test
    void testRemoveExample_whenRemoveExampleThatIsNotPresentForTheWord_thenThrow() {
        when(exampleMapper.exampleDtoToExample(any())).thenReturn(EXAMPLE);
        when(wordService.getWordByNameWithContributors(anyString())).thenReturn(Optional.of(WORD));
        when(exampleService.getExampleByText(anyString())).thenReturn(Optional.of(EXAMPLE));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(EXAMPLE_NOT_FOUND_FOR_THE_WORD);

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordFacade.removeExampleFromWord(WORD.getName(), EXAMPLE_DTO)
        );

        assertEquals(EXAMPLE_NOT_FOUND_FOR_THE_WORD, resourceNotFoundException.getMessage());
    }

    @Test
    void testGetAllSynonyms() {
        WORD.addSynonym(SYNONYM);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.of(WORD));
        when(wordMapper.wordToWordDto(any())).thenReturn(SYNONYM_DTO);

        Set<WordDto> synonyms = wordFacade.getAllSynonyms(WORD.getName());

        assertTrue(synonyms.contains(SYNONYM_DTO));
    }

    @Test
    void testGetAllAntonyms() {
        WORD.addAntonym(ANTONYM);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.of(WORD));
        when(wordMapper.wordToWordDto(any())).thenReturn(ANTONYM_DTO);

        Set<WordDto> antonyms = wordFacade.getAllAntonyms(WORD.getName());

        assertTrue(antonyms.contains(ANTONYM_DTO));
    }

    @Test
    void testAddSynonym() {
        when(wordService.getWordByNameWithContributors(WORD.getName())).thenReturn(Optional.of(WORD));
        when(wordService.getWordByNameWithContributors(SYNONYM.getName())).thenReturn(Optional.of(SYNONYM));

        wordFacade.addSynonym(WORD.getName(), SYNONYM_DTO);

        assertTrue(WORD.getSynonyms().contains(SYNONYM));
    }

    @Test
    void testAddSynonym_whenAddExistingSynonym_thenThrow() {
        WORD.addSynonym(SYNONYM);
        when(wordService.getWordByNameWithContributors(WORD.getName())).thenReturn(Optional.of(WORD));
        when(wordService.getWordByNameWithContributors(SYNONYM.getName())).thenReturn(Optional.of(SYNONYM));
        when(messageSource.getMessage(anyString(), any(), any()))
                .thenReturn(WORD_IS_ALREADY_LINKED.formatted(SYNONYM.getName(), WORD.getName()));

        DuplicateResourceException duplicateResourceException = assertThrows(
                DuplicateResourceException.class,
                () -> wordFacade.addSynonym(WORD.getName(), SYNONYM_DTO)
        );

        assertEquals(WORD_IS_ALREADY_LINKED.formatted(SYNONYM.getName(), WORD.getName()),
                duplicateResourceException.getMessage());
    }

    @Test
    void testRemoveSynonym() {
        WORD.addSynonym(SYNONYM);
        when(wordService.getWordByNameWithContributors(WORD.getName())).thenReturn(Optional.of(WORD));
        when(wordService.getWordByNameWithContributors(SYNONYM.getName())).thenReturn(Optional.of(SYNONYM));

        wordFacade.removeSynonym(WORD.getName(), SYNONYM_DTO);

        assertFalse(WORD.getSynonyms().contains(SYNONYM));
    }

    @Test
    void testRemoveSynonym_whenRemoveNonSynonym_thenThrow() {
        when(wordService.getWordByNameWithContributors(WORD.getName())).thenReturn(Optional.of(WORD));
        when(wordService.getWordByNameWithContributors(SYNONYM.getName())).thenReturn(Optional.of(SYNONYM));
        when(messageSource.getMessage(anyString(), any(), any()))
                .thenReturn(WORD_IS_NOT_LINKED.formatted(SYNONYM.getName()));

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordFacade.removeSynonym(WORD.getName(), SYNONYM_DTO)
        );

        assertEquals(WORD_IS_NOT_LINKED.formatted(SYNONYM.getName()),
                resourceNotFoundException.getMessage());
    }

    @Test
    void testAddAntonym() {
        when(wordService.getWordByNameWithContributors(WORD.getName())).thenReturn(Optional.of(WORD));
        when(wordService.getWordByNameWithContributors(ANTONYM.getName())).thenReturn(Optional.of(ANTONYM));

        wordFacade.addAntonym(WORD.getName(), ANTONYM_DTO);

        assertTrue(WORD.getAntonyms().contains(ANTONYM));
    }

    @Test
    void testAddAntonym_whenAddExistingSynonym_thenThrow() {
        WORD.addSynonym(ANTONYM);
        when(wordService.getWordByNameWithContributors(WORD.getName())).thenReturn(Optional.of(WORD));
        when(wordService.getWordByNameWithContributors(ANTONYM.getName())).thenReturn(Optional.of(ANTONYM));
        when(messageSource.getMessage(anyString(), any(), any()))
                .thenReturn(WORD_IS_ALREADY_LINKED.formatted(ANTONYM.getName(), WORD.getName()));

        DuplicateResourceException duplicateResourceException = assertThrows(
                DuplicateResourceException.class,
                () -> wordFacade.addSynonym(WORD.getName(), ANTONYM_DTO)
        );

        assertEquals(WORD_IS_ALREADY_LINKED.formatted(ANTONYM.getName(), WORD.getName()),
                duplicateResourceException.getMessage());
    }

    @Test
    void testRemoveAntonym() {
        WORD.addAntonym(ANTONYM);
        when(wordService.getWordByNameWithContributors(WORD.getName())).thenReturn(Optional.of(WORD));
        when(wordService.getWordByNameWithContributors(ANTONYM.getName())).thenReturn(Optional.of(ANTONYM));

        wordFacade.removeAntonym(WORD.getName(), ANTONYM_DTO);

        assertFalse(WORD.getAntonyms().contains(ANTONYM));
    }

    @Test
    void testRemoveAntonym_whenRemoveNonSynonym_thenThrow() {
        when(wordService.getWordByNameWithContributors(WORD.getName())).thenReturn(Optional.of(WORD));
        when(wordService.getWordByNameWithContributors(ANTONYM.getName())).thenReturn(Optional.of(ANTONYM));
        when(messageSource.getMessage(anyString(), any(), any()))
                .thenReturn(WORD_IS_NOT_LINKED.formatted(ANTONYM.getName()));

        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class,
                () -> wordFacade.removeAntonym(WORD.getName(), ANTONYM_DTO)
        );

        assertEquals(WORD_IS_NOT_LINKED.formatted(ANTONYM.getName()),
                resourceNotFoundException.getMessage());
    }

    @Test
    void testAddComment() {
        try (MockedStatic<SecurityUtils> utilsMockedStatic = mockStatic(SecurityUtils.class)) {
            when(wordService.getWordByName(anyString())).thenReturn(Optional.of(WORD));
            when(commentMapper.commentDtoToComment(any())).thenReturn(COMMENT);
            utilsMockedStatic.when(SecurityUtils::getUsername).thenReturn(USER.getEmail());
            when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));
            when(commentService.addComment(any())).thenReturn(COMMENT);
            assertDoesNotThrow(() -> wordFacade.addComment(WORD.getName(), COMMENT_DTO));
        }
    }

    @Test
    void testRemoveComment_whenRemoveExistingComment() {
        COMMENT.setId(1);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.of(WORD));
        when(commentService.getCommentById(anyInt())).thenReturn(Optional.of(COMMENT));
        doNothing().when(commentService).removeComment(COMMENT);
        assertDoesNotThrow(() -> wordFacade.removeComment(WORD.getName(), COMMENT.getId()));
    }

    @Test
    void testRemoveComment_whenRemoveNonExistingComment_thenThrow() {
        COMMENT.setId(1);
        when(wordService.getWordByName(anyString())).thenReturn(Optional.of(WORD));
        when(commentService.getCommentById(anyInt())).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(COMMENT_NOT_FOUND);
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> wordFacade.removeComment(WORD.getName(), COMMENT.getId()));

        assertEquals(COMMENT_NOT_FOUND, resourceNotFoundException.getMessage());
    }

    @Test
    void testGetAllCommentsByWord() {
        when(commentService.getAllCommentsByWord(anyString())).thenReturn(List.of(COMMENT));
        when(commentMapper.commentToCommentDto(any())).thenReturn(COMMENT_DTO);

        List<CommentDto> actualResult = wordFacade.getAllCommentsByWord(WORD.getName());
        assertTrue(actualResult.contains(COMMENT_DTO));
    }

    @AfterAll
    static void afterAll() {
        WORD.getDefinitions().clear();
        WORD_DTO.getDefinitions().clear();

        WORD.getExamples().clear();
        WORD_DTO.getExamples().clear();
    }
}