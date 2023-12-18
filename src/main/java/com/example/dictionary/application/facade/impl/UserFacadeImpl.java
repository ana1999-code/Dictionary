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
        UserInfo userInfo = new UserInfo();
        userInfo.setProgress(0);
        user.setUserInfo(userInfo);
        user.setRole(KeyRoleExtractor.getRole(userDto.getKey()));

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userService.registerUser(user);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email %s not found".formatted(email)));

        return userMapper.userToUserDto(user);
    }

    @Override
    public void updateUserProgress(UserDto user) {
        String email = user.getEmail();
        User userToUpdate = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email %s not found".formatted(email)));

        userToUpdate.getUserInfo().setProgress(user.getUserInfo().getProgress());
        userService.registerUser(userToUpdate);
    }
}
