package com.example.dictionary.domain.service;

import com.example.dictionary.domain.entity.Comment;

import java.util.Optional;

public interface CommentService {

    Comment addComment(Comment comment);

    Optional<Comment> getCommentById(Integer id);

    void removeComment(Comment comment);
}
