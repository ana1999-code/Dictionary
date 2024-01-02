package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> getAllUsers();
}
