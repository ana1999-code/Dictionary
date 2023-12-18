package com.example.dictionary.rest.controller.impl;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.InvalidPasswordException;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.UserFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static com.example.dictionary.utils.TestUtils.EMAIL_IS_TAKEN;
import static com.example.dictionary.utils.TestUtils.FIRST_NAME_IS_REQUIRED;
import static com.example.dictionary.utils.TestUtils.INCORRECT_EMAIL_FORMAT;
import static com.example.dictionary.utils.TestUtils.INVALID_KEY;
import static com.example.dictionary.utils.TestUtils.KEY_REQUIRED;
import static com.example.dictionary.utils.TestUtils.LAST_NAME_IS_REQUIRED;
import static com.example.dictionary.utils.TestUtils.PASSWORD_REQUIRED;
import static com.example.dictionary.utils.TestUtils.PASSWORD_VALIDATION_ERRORS;
import static com.example.dictionary.utils.TestUtils.USER_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerImplTest {

    @MockBean
    private UserFacade userFacade;

    @Autowired
    private MockMvc mockMvc;

    private static final String URL = "/register";

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        USER_DTO.setFirstName("John");
        USER_DTO.setLastName("Smith");
        USER_DTO.setEmail("john@mail.com");
        USER_DTO.setPassword("Pass111_");
        USER_DTO.setKey("admin");
    }

    @Test
    void testRegisterUser_whenRegisterValidUser_thenStatusIsCreated() throws Exception {
        when(userFacade.registerUser(any(UserDto.class))).thenReturn(USER_DTO);

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto registeredUser = mapper.readValue(response, UserDto.class);
        assertEquals(USER_DTO, registeredUser);
    }

    @Test
    void testRegisterUser_whenRegisterUserWithExistingEmail_thenThrow() throws Exception {
        when(userFacade.registerUser(USER_DTO))
                .thenThrow(new DuplicateResourceException(EMAIL_IS_TAKEN.formatted(USER_DTO.getEmail())));

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> errorMap = mapper.readValue(response, HashMap.class);
        assertEquals(EMAIL_IS_TAKEN.formatted(USER_DTO.getEmail()), errorMap.get("error"));
    }

    @Test
    void testRegisterUser_whenRegisterUserWithoutFirstName_thenThrow() throws Exception {
        USER_DTO.setFirstName(null);

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> errorMap = mapper.readValue(response, HashMap.class);
        assertEquals(FIRST_NAME_IS_REQUIRED, errorMap.get("firstName"));
    }

    @Test
    void testRegisterUser_whenRegisterUserWithoutLastName_thenThrow() throws Exception {
        USER_DTO.setLastName(null);

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> errorMap = mapper.readValue(response, HashMap.class);
        assertEquals(LAST_NAME_IS_REQUIRED, errorMap.get("lastName"));
    }

    @Test
    void testRegisterUser_whenRegisterUserWithInvalidEmail_thenThrow() throws Exception {
        USER_DTO.setEmail("incorrect");

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> errorMap = mapper.readValue(response, HashMap.class);
        assertEquals(INCORRECT_EMAIL_FORMAT, errorMap.get("email"));
    }

    @Test
    void testRegisterUser_whenRegisterUserWithoutPassword_thenThrow() throws Exception {
        USER_DTO.setPassword(null);

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);
        assertEquals(PASSWORD_REQUIRED, actualErrorMap.get("password"));
    }

    @Test
    void testRegisterUser_whenRegisterUserWithInvalidPassword_thenThrow() throws Exception {
        when(userFacade.registerUser(USER_DTO))
                .thenThrow(new InvalidPasswordException(PASSWORD_VALIDATION_ERRORS));

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);
        assertEquals(PASSWORD_VALIDATION_ERRORS, actualErrorMap);
    }

    @Test
    void testRegisterUser_whenRegisterUserWithoutKey_thenThrow() throws Exception {
        USER_DTO.setKey(null);

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);
        assertEquals(KEY_REQUIRED, actualErrorMap.get("key"));
    }

    @Test
    void testRegisterUser_whenRegisterUserWithInvalidKey_thenThrow() throws Exception {
        when(userFacade.registerUser(any(UserDto.class)))
                .thenThrow(new ResourceNotFoundException(INVALID_KEY));

        String response = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> actualErrorMap = mapper.readValue(response, HashMap.class);
        assertEquals(INVALID_KEY, actualErrorMap.get("error"));
    }
}