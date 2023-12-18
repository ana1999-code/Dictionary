package com.example.dictionary.application.validator;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.domain.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    public void validate(UserDto userDto) {
        String email = userDto.getEmail();

        if (userService.existsUserByEmail(email)) {
            throw new DuplicateResourceException("Email [%s] is already taken".formatted(email));
        }

        PasswordValidator.validate(userDto.getPassword());
    }
}
