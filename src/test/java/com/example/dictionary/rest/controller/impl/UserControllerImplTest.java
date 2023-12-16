package com.example.dictionary.rest.controller.impl;

import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.security.role.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.dictionary.application.security.role.Role.ADMIN;
import static com.example.dictionary.utils.TestUtils.USER_DTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerImplTest.class)
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
        USER_DTO.setRole(ADMIN);
    }

    @Test
    void testRegisterUser_whenRegisterValidUser_thenStatusIsCreated() throws Exception {
        doNothing().when(userFacade).registerUser(USER_DTO);

        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testRegisterUser_whenRegisterUserWithExistingUsername_thenThrow() {

    }
}