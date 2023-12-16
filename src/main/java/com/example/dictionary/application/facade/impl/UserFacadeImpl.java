package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.mapper.UserMapper;
import com.example.dictionary.application.validator.UserValidator;
import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserValidator userValidator;

    private final PasswordEncoder passwordEncoder;

    public UserFacadeImpl(UserService userService,
                          UserMapper userMapper,
                          UserValidator userValidator,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserDto userDto) {
        userValidator.validate(userDto);

        User user = userMapper.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userService.registerUser(user);
    }
}
