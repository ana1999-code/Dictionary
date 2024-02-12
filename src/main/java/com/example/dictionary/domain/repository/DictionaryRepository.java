package com.example.dictionary.domain.repository;

import com.example.dictionary.domain.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    Optional<Dictionary> findDictionaryByName(String name);

    boolean existsByName(String name);

    boolean existsByUrl(String url);
}
