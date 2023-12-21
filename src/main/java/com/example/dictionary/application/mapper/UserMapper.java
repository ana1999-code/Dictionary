package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.security.auth.ApplicationUser;
import com.example.dictionary.application.security.role.Role;
import com.example.dictionary.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

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
    UserDto userToUserDto(User user);

    @Named("roleToGrantedAuthorities")
    static Set<GrantedAuthority> roleToGrantedAuthorities(Role role) {
        return role.getGrantedAuthorities();
    }
}
