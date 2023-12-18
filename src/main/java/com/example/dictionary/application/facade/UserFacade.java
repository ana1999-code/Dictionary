package com.example.dictionary.application.facade;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.domain.entity.User;

public interface UserFacade {

    UserDto registerUser(UserDto userDto);

    UserDto findUserByEmail(String email);

    Integer updateUserProgress(UserDto user);
}
