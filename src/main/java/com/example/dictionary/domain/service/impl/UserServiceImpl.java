package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.repository.UserRepository;
import com.example.dictionary.domain.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.dictionary.application.cache.CacheContext.USERS_CACHE;
import static com.example.dictionary.application.cache.CacheContext.USER_CACHE;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @CacheEvict(value = USERS_CACHE, allEntries = true)
    @CachePut(value = USER_CACHE, key = "#result.email")
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    @Cacheable(value = USER_CACHE, key = "#email")
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Cacheable(value = USERS_CACHE, keyGenerator = "cacheKeyGenerator")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
