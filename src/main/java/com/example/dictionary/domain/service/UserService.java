package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.User;

public interface UserService {

    void registerUser(User user);

    boolean existsUserByEmail(String email);
}
