package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.User;

import java.util.Optional;

public interface UserService {

    void registerUser(User user);

    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);
}
