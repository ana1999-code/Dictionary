package com.example.dictionary.domain.repository;

import com.example.dictionary.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c INNER JOIN c.word w WHERE w.name = :name")
    List<Comment> findAllCommentsByWord(@Param("name") String name);
}
