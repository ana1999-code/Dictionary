package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.util.ImageUtils;
import com.example.dictionary.domain.entity.Comment;
import com.example.dictionary.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface CommentMapper {

    Comment commentDtoToComment(CommentDto commentDto);

    @Mapping(target = "commenter", source = "commenter", qualifiedByName = "commenterToCommenterDto")
    @Mapping(target = "word", ignore = true)
    CommentDto commentToCommentDto(Comment comment);

    @Named("commenterToCommenterDto")
    static UserDto commenterToCommenterDto(User user) {
        byte[] logo = null;
        byte[] dbLogo = user.getProfileImage();

        if (dbLogo != null) {
            logo = ImageUtils.decompressImage(dbLogo);
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setProfileImage(logo);

        return userDto;
    }
}
