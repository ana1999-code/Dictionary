package com.example.dictionary.domain.repository;

import com.example.dictionary.domain.entity.Definition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DefinitionRepository extends JpaRepository<Definition, Integer> {

    @Query("SELECT d FROM Definition d WHERE d.text = :text")
    Optional<Definition> getDefinitionByText(@Param("text") String text);
}
