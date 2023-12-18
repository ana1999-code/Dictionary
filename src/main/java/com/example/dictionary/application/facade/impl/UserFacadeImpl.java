package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.mapper.UserMapper;
import com.example.dictionary.application.security.key.KeyRoleExtractor;
import com.example.dictionary.application.validator.UserValidator;
import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.entity.UserInfo;
import com.example.dictionary.domain.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
    public UserDto registerUser(UserDto userDto) {
        userValidator.validate(userDto);

        User user = userMapper.userDtoToUser(userDto);
        UserInfo userInfo = new UserInfo(0);
        user.setUserInfo(userInfo);
        user.setRole(KeyRoleExtractor.getRole(userDto.getKey()));
        user.setRegisteredAt(LocalDate.now());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User registeredUser = userService.registerUser(user);
        return userMapper.userToUserDto(registeredUser);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = getUser(email);

        return userMapper.userToUserDto(user);
    }

    @Override
    public Integer updateUserProgress(UserDto user) {
        String email = user.getEmail();
        User userToUpdate = getUser(email);

        Integer progress = userToUpdate.getUserInfo().getProgress() + 1;

        userToUpdate.getUserInfo().setProgress(progress);
        User updatedUser = userService.registerUser(userToUpdate);
        return updatedUser.getUserInfo().getProgress();
    }

    private User getUser(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email %s not found".formatted(email)));
    }
}
