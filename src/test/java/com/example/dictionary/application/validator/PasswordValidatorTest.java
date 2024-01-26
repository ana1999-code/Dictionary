package com.example.dictionary.application.validator;

import com.example.dictionary.application.exception.InvalidPasswordException;
import com.example.dictionary.application.validator.password.PasswordValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {

    @ParameterizedTest
    @CsvSource({"Pass111_"})
    void testValidatePassword_whenProvideValidPassword_thenReturnTrue(String password) {
        assertDoesNotThrow(() -> PasswordValidator.validate(password));
    }

    @ParameterizedTest
    @CsvSource({
            "pass12_,Password should have at least 8 chars",
            "pass1111_,Password should have at least one uppercase char",
            "PASS1111_,Password should have at least one lowercase char",
            "Password_,Password should have at least one number",
            "Password1,Password should have at least one special char",
            "Pass 1___,Password should not have any whitespaces"
    })
    void testValidatePassword_whenProvideInvalidPassword_thenThrow(String password, String message) {
        InvalidPasswordException invalidPasswordException = assertThrows(InvalidPasswordException.class,
                () -> PasswordValidator.validate(password));

        Collection<String> messages = invalidPasswordException.getErrorMap().values();

        assertTrue(messages.contains(message));
    }
}