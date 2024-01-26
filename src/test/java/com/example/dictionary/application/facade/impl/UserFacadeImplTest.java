package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.mapper.UserMapper;
import com.example.dictionary.application.validator.UserValidator;
import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.entity.UserInfo;
import com.example.dictionary.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.dictionary.application.security.role.Role.ADMIN;
import static com.example.dictionary.utils.TestUtils.USER;
import static com.example.dictionary.utils.TestUtils.USER_DTO;
import static com.example.dictionary.utils.TestUtils.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFacadeImplTest {

    @InjectMocks
    private UserFacadeImpl userFacade;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserValidator userValidator;

    @Mock
    private MessageSource messageSource;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        USER_DTO.setFirstName("John");
        USER_DTO.setLastName("Smith");
        USER_DTO.setEmail("john@mail.com");
        USER_DTO.setPassword("Pass1234_");
        USER_DTO.setKey("admin");

        USER.setFirstName("John");
        USER.setLastName("Smith");
        USER.setEmail("john@mail.com");
        USER.setPassword("Pass1234_");
        USER.setRole(ADMIN);
        USER.setRegisteredAt(LocalDate.now());
        USER.setUserInfo(new UserInfo(0));
    }

    @Test
    void testRegisterUser_whenRegisterValidUser() {
        doNothing().when(userValidator).validate(any(UserDto.class));
        when(userMapper.userDtoToUser(any(UserDto.class))).thenReturn(USER);
        when(userService.registerUser(any(User.class))).thenReturn(USER);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(USER_DTO);

        UserDto registeredUser = userFacade.registerUser(USER_DTO);

        assertEquals(USER_DTO, registeredUser);

        verify(userService).registerUser(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertEquals(capturedUser, USER);
        verify(userValidator).validate(USER_DTO);
        verify(userMapper).userDtoToUser(any(UserDto.class));
        verify(userService).registerUser(any(User.class));
    }

    @Test
    void testRegisterUser_whenRegisterUserWithTakenEmail_thenThrow() {
        doThrow(DuplicateResourceException.class).when(userValidator).validate(any());

        DuplicateResourceException duplicateResourceException = assertThrows(
                DuplicateResourceException.class,
                () -> userFacade.registerUser(USER_DTO)
        );

        assertEquals(DuplicateResourceException.class, duplicateResourceException.getClass());

        verify(userValidator).validate(USER_DTO);
    }

    @Test
    void testFindUserByEmail_whenProvideExistingEmail_thenReturnUser() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));
        when(userMapper.userToUserDto(any(User.class))).thenReturn(USER_DTO);

        UserDto actualUser = userFacade.findUserByEmail(USER.getEmail());

        assertEquals(USER_DTO, actualUser);
        verify(userService).findByEmail(anyString());
        verify(userMapper).userToUserDto(any());
    }

    @Test
    void testFindUserByEmail_whenProvideInvalidEmail_thenThrow() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any()))
                .thenReturn(USER_NOT_FOUND.formatted(USER.getEmail()));

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> userFacade.findUserByEmail(USER.getEmail()));

        assertEquals(USER_NOT_FOUND.formatted(USER.getEmail()), resourceNotFoundException.getMessage());
        verify(userService).findByEmail(anyString());
    }

    @Test
    void testUpdateUserProgress_whenUserExists_thenUpdateProgress() {
        int expectedProgress = 1;
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));
        when(userService.registerUser(any())).thenReturn(USER);

        Integer actualProgress = userFacade.updateUserProgress(USER_DTO);

        assertEquals(expectedProgress, actualProgress);

        verify(userService).registerUser(userArgumentCaptor.capture());
        User updatedUser = userArgumentCaptor.getValue();
        assertEquals(updatedUser.getUserInfo().getProgress(), actualProgress);
    }

    @Test
    void testUpdateUserProgress_whenUserDoesNotExist_thenThrow() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any()))
                .thenReturn(USER_NOT_FOUND.formatted(USER.getEmail()));

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> userFacade.updateUserProgress(USER_DTO));

        assertEquals(USER_NOT_FOUND.formatted(USER.getEmail()), resourceNotFoundException.getMessage());
        verify(userService).findByEmail(anyString());
    }
}