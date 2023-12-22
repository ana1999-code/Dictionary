package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.entity.Word;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

@Mapper
public interface WordMapper {

    @Mapping(target = "synonyms", ignore = true)
    @Mapping(target = "antonyms", ignore = true)
    Word wordDtoToWord(WordDto wordDto);

    @Mapping(target = "synonyms", ignore = true)
    @Mapping(target = "antonyms", ignore = true)
    @Mapping(target = "contributors", source = "contributors", qualifiedByName = "userToUserDtoNames")
    WordDto wordToWordDto(Word word);

    @Named("userToUserDtoNames")
    static Set<UserDto> userToUserDtoNames(Set<User> users) {
        Set<UserDto> userDtos = new HashSet<>();
        users
                .forEach(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setFirstName(user.getFirstName());
                    userDto.setLastName(user.getLastName());
                    userDto.setEmail(user.getEmail());
                    userDto.setRegisteredAt(user.getRegisteredAt());
                    userDtos.add(userDto);
                });

        return userDtos;
    }
}
