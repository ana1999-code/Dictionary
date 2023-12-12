package com.example.dictionary.domain.repository;

import com.example.dictionary.domain.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Integer> {

    Optional<Word> findByName(String name);
}
