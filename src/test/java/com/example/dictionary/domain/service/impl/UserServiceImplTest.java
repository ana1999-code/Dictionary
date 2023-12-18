package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.dictionary.application.security.role.Role.ADMIN;
import static com.example.dictionary.utils.TestUtils.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        USER.setFirstName("John");
        USER.setLastName("Smith");
        USER.setEmail("john@mail.com");
        USER.setPassword("Pass1234_");
        USER.setRole(ADMIN);
        USER.setRegisteredAt(LocalDate.now());
    }

    @Test
    void testRegisterUser() {
        when(userRepository.save(any(User.class))).thenReturn(USER);

        User registeredUser = userService.registerUser(USER);

        assertEquals(USER, registeredUser);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testExistsUserByEmail_whenUserExists_thenReturnTrue() {
        when(userRepository.existsUserByEmail(anyString())).thenReturn(true);

        boolean exists = userService.existsUserByEmail(USER.getEmail());

        assertTrue(exists);
        verify(userRepository).existsUserByEmail(anyString());
    }

    @Test
    void testExistsUserByEmail_whenUserDoesNotExist_thenReturnFalse() {
        when(userRepository.existsUserByEmail(anyString())).thenReturn(false);

        boolean exists = userService.existsUserByEmail(USER.getEmail());

        assertFalse(exists);
        verify(userRepository).existsUserByEmail(anyString());
    }

    @Test
    void testFindUserByEmail_whenUserExists_thenReturnUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(USER));

        Optional<User> actualUser = userService.findByEmail(USER.getEmail());

        assertEquals(USER, actualUser.get());
        verify(userRepository).findByEmail(anyString());
    }

    @Test
    void testFindUserByEmail_whenUserDoesNotExist_thenReturnEmpty() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<User> actualUser = userService.findByEmail(USER.getEmail());

        assertTrue(actualUser.isEmpty());
        verify(userRepository).findByEmail(anyString());
    }
}