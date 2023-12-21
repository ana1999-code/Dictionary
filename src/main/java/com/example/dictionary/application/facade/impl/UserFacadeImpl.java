package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.AchievementDto;
import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.mapper.AchievementMapper;
import com.example.dictionary.application.mapper.UserMapper;
import com.example.dictionary.application.security.key.KeyRoleExtractor;
import com.example.dictionary.application.security.utils.SecurityUtils;
import com.example.dictionary.application.util.ImageUtil;
import com.example.dictionary.application.validator.UserValidator;
import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.entity.UserInfo;
import com.example.dictionary.domain.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserValidator userValidator;

    private final PasswordEncoder passwordEncoder;

    private final AchievementMapper achievementMapper;

    public UserFacadeImpl(UserService userService,
                          UserMapper userMapper,
                          UserValidator userValidator,
                          PasswordEncoder passwordEncoder,
                          AchievementMapper achievementMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.achievementMapper = achievementMapper;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        userValidator.validate(userDto);

        User user = userMapper.userDtoToUser(userDto);

        user.setRole(KeyRoleExtractor.getRole(userDto.getKey()));
        user.setRegisteredAt(LocalDate.now());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setUserInfo(new UserInfo(0));
        User registeredUser = userService.registerUser(user);
        return userMapper.userToUserDto(registeredUser);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = getUser(email);

        return userMapper.userToUserDto(user);
    }

    @Override
    public Integer updateUserProgress(UserDto user) {
        String email = user.getEmail();
        User userToUpdate = getUser(email);

        Integer progress = userToUpdate.getUserInfo().getProgress() + 1;

        userToUpdate.getUserInfo().setProgress(progress);
        User updatedUser = userService.registerUser(userToUpdate);
        return updatedUser.getUserInfo().getProgress();
    }

    @Override
    public UserDto uploadImage(MultipartFile file) throws IOException {
        User user = getUser(SecurityUtils.getUsername());
        user.setProfileImage(ImageUtil.compressImage(file.getBytes()));

        User updatedUser = userService.registerUser(user);
        byte[] addedLogo = ImageUtil.decompressImage(updatedUser.getProfileImage());

        UserDto userDto = userMapper.userToUserDto(user);
        userDto.setProfileImage(addedLogo);
        return userDto;
    }

    @Override
    public UserDto getUserProfile() {
        User user = getUser(SecurityUtils.getUsername());

        byte[] logo = null;
        byte[] dbLogo = user.getProfileImage();

        if (dbLogo != null) {
            logo = ImageUtil.decompressImage(dbLogo);
        }

        UserDto userDto = userMapper.userToUserDto(user);
        userDto.setProfileImage(logo);

        return userDto;
    }

    @Override
    public boolean updateUser(UserDto userDto) {
        User user = getUser(SecurityUtils.getUsername());

        boolean isChanged = setUserCredentials(userDto, user);

        userService.registerUser(user);
        return isChanged;
    }

    @Override
    public void addAchievement(AchievementDto achievement) {
        User user = getUser(SecurityUtils.getUsername());

        user.getUserInfo()
                .getAchievements()
                .add(achievementMapper.achievementDtoToAchievement(achievement));

        userService.registerUser(user);
    }

    private static boolean setUserCredentials(UserDto userDto, User user) {
        boolean isChanged = false;

        if (userDto.getFirstName() != null && !userDto.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(userDto.getFirstName());
            isChanged = true;
        }

        if (userDto.getLastName() != null && !userDto.getLastName().equals(user.getLastName())) {
            user.setLastName(userDto.getLastName());
            isChanged = true;
        }

        return isChanged;
    }

    private User getUser(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email [%s] not found".formatted(email)));
    }
}
