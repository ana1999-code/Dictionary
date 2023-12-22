package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.security.auth.ApplicationUser;
import com.example.dictionary.application.security.role.Role;
import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.entity.Word;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Mapper
public interface UserMapper {

    @Mapping(source = "role", target = "grantedAuthorities", qualifiedByName = "roleToGrantedAuthorities")
    ApplicationUser userToApplicationUser(User user);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "capitalize")
    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "capitalize")
    User userDtoToUser(UserDto userDto);

    @Mapping(target = "key", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(source = "userInfo.favorites", target = "userInfo.favorites", qualifiedByName = "wordToWordDtoNames")
    UserDto userToUserDto(User user);

    @Named("roleToGrantedAuthorities")
    static Set<GrantedAuthority> roleToGrantedAuthorities(Role role) {
        return role.getGrantedAuthorities();
    }

    @Named("wordToWordDtoNames")
    static Set<WordDto> wordToWordDtoNames(Set<Word> words) {
        Set<WordDto> wordDtos = new HashSet<>();
        words
                .forEach(word -> {
                    WordDto wordDto = new WordDto();
                    wordDto.setName(word.getName());
                    wordDtos.add(wordDto);
                });
        return wordDtos;
    }

    @Named("capitalize")
    static String capitalizeFirstLetter(String value) {
        if (value != null && !value.isEmpty()) {
            return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
        }
        return value;
    }
}
