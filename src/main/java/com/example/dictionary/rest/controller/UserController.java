package com.example.dictionary.rest.controller;

import com.example.dictionary.application.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserController {

    ResponseEntity<UserDto> registerUser(UserDto userDao);
}
