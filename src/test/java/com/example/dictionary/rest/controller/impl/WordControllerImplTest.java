package com.example.dictionary.rest.controller.impl;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.WordFacade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.dictionary.utils.TestUtils.DEFINITION_NOT_FOUND_FOR_WORD;
import static com.example.dictionary.utils.TestUtils.DUPLICATE_WORD;
import static com.example.dictionary.utils.TestUtils.EXAMPLE_NOT_CONTAINS_WORD;
import static com.example.dictionary.utils.TestUtils.INVALID_WORD;
import static com.example.dictionary.utils.TestUtils.WORD;
import static com.example.dictionary.utils.TestUtils.WORD_DTO;
import static com.example.dictionary.utils.TestUtils.WORD_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WordControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class WordControllerImplTest {

    public static final String URL = "/words";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordFacade wordFacade;

    public static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        WORD_DTO.setName("test");
    }

    @Test
    void testGetAllWords() throws Exception {
        when(wordFacade.getAllWords()).thenReturn(List.of(WORD_DTO));

        String response = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<WordDto> actualWordList = mapper.readValue(response, new TypeReference<>() {
        });

        assertTrue(actualWordList.contains(WORD_DTO));
        verify(wordFacade).getAllWords();
    }

    @Test
    void testGetWordByName_whenGetWordByName_thenReturnWordDto() throws Exception {
        when(wordFacade.getWordByName(anyString())).thenReturn(WORD_DTO);

        String response = mockMvc.perform(get(URL + "/{name}", WORD_DTO.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        WordDto actualWord = mapper.readValue(response, WordDto.class);

        assertEquals(WORD_DTO, actualWord);
        verify(wordFacade).getWordByName(anyString());
    }

    @Test
    void testGetWordByName_whenGetWordByInvalidName_thenThrow() throws Exception {
        when(wordFacade.getWordByName(anyString())).thenThrow(new ResourceNotFoundException(WORD_NOT_FOUND));

        String response = mockMvc.perform(get(URL + "/{name}", WORD_DTO.getName()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);

        assertEquals(WORD_NOT_FOUND, actualErrorMap.get("error"));
        verify(wordFacade).getWordByName(anyString());
    }

    @Test
    void testAddWord_whenAddValidWord_thenReturnTheWord() throws Exception {
        when(wordFacade.addWord(any(WordDto.class))).thenReturn(WORD_DTO);

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(WORD_DTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        WordDto addedWord = mapper.readValue(response, WordDto.class);

        assertEquals(WORD_DTO, addedWord);
    }

    @Test
    void testAddWord_whenAddWordWithExistingName_thenThrow() throws Exception {
        when(wordFacade.addWord(any(WordDto.class)))
                .thenThrow(new DuplicateResourceException(
                        DUPLICATE_WORD
                ));

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(WORD_DTO)))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);

        assertEquals(DUPLICATE_WORD, actualErrorMap.get("error"));
        verify(wordFacade).addWord(any(WordDto.class));
    }

    @Test
    void testAddWord_whenAddWordWithoutDefinition_thenThrow() throws Exception {
        when(wordFacade.addWord(any(WordDto.class)))
                .thenThrow(new ResourceNotFoundException(
                        DEFINITION_NOT_FOUND_FOR_WORD
                ));

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(WORD_DTO)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);

        assertEquals(DEFINITION_NOT_FOUND_FOR_WORD, actualErrorMap.get("error"));
        verify(wordFacade).addWord(any(WordDto.class));
    }

    @Test
    void testAddWord_whenWordContainExampleWithoutTheWord_thenThrow() throws Exception {
        when(wordFacade.addWord(any(WordDto.class)))
                .thenThrow(new ResourceNotFoundException(
                        EXAMPLE_NOT_CONTAINS_WORD
                ));

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(WORD_DTO)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);

        assertEquals(EXAMPLE_NOT_CONTAINS_WORD, actualErrorMap.get("error"));
        verify(wordFacade).addWord(any(WordDto.class));
    }

    @Test
    void testAddWord_whenAddWordWithInvalidName_thenThrow() throws Exception {
        WORD_DTO.setName("test123");

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(WORD_DTO)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);

        assertEquals(INVALID_WORD, actualErrorMap.get("name"));
    }

    @Test
    void testDeleteWordByName_whenWordIsFound_thenReturnNoContent() throws Exception {
        doNothing().when(wordFacade).deleteWordByName(WORD.getName());

        mockMvc.perform(delete(URL + "/{name}", WORD.getName())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testDeleteWordByName_whenDeleteNonExistingWord_thenThrow() throws Exception {
        doThrow(new ResourceNotFoundException(WORD_NOT_FOUND)).when(wordFacade).deleteWordByName(WORD.getName());

        String response = mockMvc.perform(delete(URL + "/{name}", WORD.getName())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);

        assertEquals(WORD_NOT_FOUND, actualErrorMap.get("error"));
        verify(wordFacade).deleteWordByName(anyString());
    }
}