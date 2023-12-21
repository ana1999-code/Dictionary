package com.example.dictionary.rest.controller;

import com.example.dictionary.application.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserController {

    ResponseEntity<UserDto> registerUser(UserDto userDao);

    ResponseEntity<UserDto> uploadImage(MultipartFile file) throws IOException;

    ResponseEntity<UserDto> getUserProfile();
}
