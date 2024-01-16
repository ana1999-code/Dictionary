package com.example.dictionary.domain.repository;

import com.example.dictionary.application.report.data.WordDetail;
import com.example.dictionary.domain.entity.Word;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Integer> {

    Optional<Word> findByName(String name);

    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM Word w WHERE w.name = :name")
    void deleteByName(@Param("name") String name);

    @Query("SELECT new com.example.dictionary.application.report.data.WordDetail(w.id, w.name, c.name, u, w.addedAt)" +
            " FROM Word w" +
            " LEFT JOIN w.contributors u " +
            " LEFT JOIN w.category c")
    List<WordDetail> getWordsDetails();

    @EntityGraph(attributePaths = {"synonyms", "antonyms", "contributors", "examples", "definitions", "comments"})
    @Query("SELECT w FROM Word w WHERE w.name = :name")
    Optional<Word> findByNameWithContributors(@Param("name") String name);

    @Query("SELECT d.text FROM Word w JOIN w.definitions d WHERE w.name = :name")
    List<String> getWordDefinitions(@Param("name") String name);

    @Query("SELECT d.text FROM Word w RIGHT JOIN w.definitions d WHERE w.name != :name")
    List<String> getDefinitionsNotIncludedForWord(@Param("name") String name);
}
