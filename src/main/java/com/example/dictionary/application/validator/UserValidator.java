package com.example.dictionary.application.validator;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.validator.password.PasswordValidator;
import com.example.dictionary.domain.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UserValidator {

    private final MessageSource messageSource;

    private final UserService userService;

    public UserValidator(MessageSource messageSource, UserService userService) {
        this.messageSource = messageSource;
        this.userService = userService;
    }

    public void validate(UserDto userDto) {
        String email = userDto.getEmail();

        if (userService.existsUserByEmail(email)) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("email.error.message", new Object[]{email}, Locale.getDefault()));
        }

        PasswordValidator.validate(userDto.getPassword());
    }
}
