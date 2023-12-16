package com.example.dictionary.application.security.auth;

import com.example.dictionary.application.exception.IncorrectUsernameException;
import com.example.dictionary.application.mapper.UserMapper;
import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public ApplicationUserDetailService(UserRepository userRepository,
                                        UserMapper userMapper,
                                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IncorrectUsernameException("User with email %s not found".formatted(username)));

        return userMapper.userToApplicationUser(user);
    }
}
