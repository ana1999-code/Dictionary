package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Comment;
import com.example.dictionary.domain.repository.CommentRepository;
import com.example.dictionary.domain.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
