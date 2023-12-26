package com.example.dictionary.rest.controller.impl;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.rest.controller.UserController;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class UserControllerImpl implements UserController {

    private final UserFacade userFacade;

    public UserControllerImpl(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    @PostMapping("${api.registration}")
    @PermitAll
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userFacade.registerUser(userDto), CREATED);
    }

    @Override
    @PostMapping("profile")
    @PermitAll
    public ResponseEntity<UserDto> uploadImage(@RequestParam("logo") MultipartFile file) throws IOException {
        return new ResponseEntity<>(userFacade.uploadImage(file), OK);
    }

    @Override
    @GetMapping("profile")
    @PermitAll
    public ResponseEntity<UserDto> getUserProfile() {
        return new ResponseEntity<>(userFacade.getUserProfile(), OK);
    }

    @Override
    @PostMapping("favorites")
    public ResponseEntity<Set<String>> addFavoriteWord(@RequestParam("word") String wordName) {
        return new ResponseEntity<>(userFacade.addWordToFavorities(wordName), OK);
    }

    @Override
    @DeleteMapping("favorites")
    public ResponseEntity<Set<String>> removeFavoriteWord(@RequestParam("word") String wordName) {
        return new ResponseEntity<>(userFacade.removeWordFromFavorites(wordName), OK);
    }
}
