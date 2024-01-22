package com.example.dictionary.application.facade;

import com.example.dictionary.application.dto.AchievementDto;
import com.example.dictionary.application.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface UserFacade {

    UserDto registerUser(UserDto userDto);

    UserDto findUserByEmail(String email);

    Integer updateUserProgress(UserDto user);

    UserDto uploadImage(MultipartFile file) throws IOException;

    UserDto getUserProfile();

    boolean updateUser(UserDto userDto);

    void addAchievement(AchievementDto achievement);

    Set<String> addWordToFavorities(String wordName);

    Set<String> removeWordFromFavorites(String wordName);

    List<UserDto> getAllUsers();
}
