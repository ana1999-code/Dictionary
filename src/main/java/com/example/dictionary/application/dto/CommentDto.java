package com.example.dictionary.application.dto;

import java.time.LocalDate;

public class CommentDto {

    private Integer id;

    private String text;

    private UserDto commenter;

    private LocalDate commentedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDto getCommenter() {
        return commenter;
    }

    public void setCommenter(UserDto commenter) {
        this.commenter = commenter;
    }

    public LocalDate getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(LocalDate commentedAt) {
        this.commentedAt = commentedAt;
    }
}
