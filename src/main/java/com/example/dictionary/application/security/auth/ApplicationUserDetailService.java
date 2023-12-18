package com.example.dictionary.application.security.auth;

import com.example.dictionary.application.exception.IncorrectUsernameException;
import com.example.dictionary.application.mapper.UserMapper;
import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailService implements UserDetailsService {

    private final UserService userService;

    private final UserMapper userMapper;

    public ApplicationUserDetailService(UserService userService,
                                        UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByEmail(username)
                .orElseThrow(() -> new IncorrectUsernameException("User with email %s not found".formatted(username)));

        return userMapper.userToApplicationUser(user);
    }
}
