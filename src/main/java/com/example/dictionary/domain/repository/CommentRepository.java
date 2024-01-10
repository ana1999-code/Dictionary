package com.example.dictionary.domain.repository;

import com.example.dictionary.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
