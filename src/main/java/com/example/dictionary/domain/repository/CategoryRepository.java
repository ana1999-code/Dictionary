package com.example.dictionary.domain.repository;

import com.example.dictionary.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> getCategoryByName(@Param("name") String name);
}
