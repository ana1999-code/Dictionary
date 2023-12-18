package com.example.dictionary.rest.controller.impl;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.rest.controller.UserController;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

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
}
