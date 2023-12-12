package com.example.dictionary.rest.controller.impl;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.WordFacade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.dictionary.utils.TestUtils.TEST_DTO;
import static com.example.dictionary.utils.TestUtils.WORD_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WordControllerImpl.class)
class WordControllerImplTest {

    public static final String URL = "/words";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordFacade wordFacade;

    public static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testGetAllWords() throws Exception {
        when(wordFacade.getAllWords()).thenReturn(List.of(TEST_DTO));

        String actualResult = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<WordDto> actualWordList = mapper.readValue(actualResult, new TypeReference<>() {
        });

        assertTrue(actualWordList.contains(TEST_DTO));
        verify(wordFacade).getAllWords();
    }

    @Test
    void testGetWordByName_whenGetWordByName_thenReturnWordDto() throws Exception {
        when(wordFacade.getWordByName(anyString())).thenReturn(TEST_DTO);

        String actualResult = mockMvc.perform(get(URL + "/{name}", TEST_DTO.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        WordDto actualWord = mapper.readValue(actualResult, WordDto.class);

        assertEquals(TEST_DTO, actualWord);
        verify(wordFacade).getWordByName(anyString());
    }

    @Test
    void testGetWordByName_whenGetWordByInvalidName_thenThrow() throws Exception {
        when(wordFacade.getWordByName(anyString())).thenThrow(new ResourceNotFoundException(WORD_NOT_FOUND));

        String contentAsString = mockMvc.perform(get(URL + "/{name}", TEST_DTO.getName()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> actualErrorMap = mapper.readValue(contentAsString, HashMap.class);

        assertEquals(WORD_NOT_FOUND, actualErrorMap.get("error"));
        verify(wordFacade).getWordByName(anyString());
    }
}