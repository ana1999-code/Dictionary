package com.example.dictionary.application.mapper;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.domain.entity.Comment;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper {

    Comment commentDtoToComment(CommentDto commentDto);

    CommentDto commentToCommentDto(Comment comment);
}
