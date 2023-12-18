package com.example.dictionary.application.validator;

import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.InvalidPasswordException;
import com.example.dictionary.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.dictionary.utils.TestUtils.EMAIL_IS_TAKEN;
import static com.example.dictionary.utils.TestUtils.PASSWORD_VALIDATION_ERRORS;
import static com.example.dictionary.utils.TestUtils.USER_DTO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @InjectMocks
    private UserValidator userValidator;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        USER_DTO.setFirstName("John");
        USER_DTO.setLastName("Smith");
        USER_DTO.setEmail("john@mail.com");
        USER_DTO.setPassword("Pass111_");
        USER_DTO.setKey("admin");
    }

    @Test
    void testValidate_whenUserIsValid_thenDoNotThrow() {
        when(userService.existsUserByEmail(anyString())).thenReturn(false);

        assertDoesNotThrow(() -> userValidator.validate(USER_DTO));
    }

    @Test
    void testValidate_whenUserEmailIsTaken_thenThrow() {
        when(userService.existsUserByEmail(anyString())).thenReturn(true);

        DuplicateResourceException duplicateResourceException = assertThrows(DuplicateResourceException.class,
                () -> userValidator.validate(USER_DTO));

        assertEquals(EMAIL_IS_TAKEN.formatted(USER_DTO.getEmail()), duplicateResourceException.getMessage());
    }

    @Test
    void testValidate_whenIncorrectPassword_thenThrow() {
        try (MockedStatic<PasswordValidator> passwordValidatorMocked =
                     mockStatic(PasswordValidator.class)) {
            passwordValidatorMocked.when(() -> PasswordValidator.validate(anyString()))
                    .thenThrow(new InvalidPasswordException(PASSWORD_VALIDATION_ERRORS));

            InvalidPasswordException invalidPasswordException = assertThrows(InvalidPasswordException.class,
                    () -> userValidator.validate(USER_DTO));

            assertEquals(PASSWORD_VALIDATION_ERRORS, invalidPasswordException.getErrorMap());
        }
    }
}