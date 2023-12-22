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
        words.stream()
                .forEach(word -> {
                    WordDto wordDto = new WordDto();
                    wordDto.setName(word.getName());
                    wordDtos.add(wordDto);
                });
        return wordDtos;
    }
}
