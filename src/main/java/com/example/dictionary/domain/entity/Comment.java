package com.example.dictionary.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.DATE;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String text;

    @OneToOne
    private User commenter;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(value = DATE)
    private LocalDate commentedAt;

    @ManyToOne
    @JoinColumn
    private Word word;

    public Comment() {
    }

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

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public LocalDate getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(LocalDate commentedAt) {
        this.commentedAt = commentedAt;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
