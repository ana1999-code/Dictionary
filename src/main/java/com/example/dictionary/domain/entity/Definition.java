package com.example.dictionary.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "definitions")
public class Definition {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String text;

    @ManyToMany(mappedBy = "definitions", fetch = LAZY)
    private Set<Word> words = new HashSet<>();

    @ManyToMany(fetch = LAZY)
    @JoinTable(
            name = "definition_dictionary",
            joinColumns = @JoinColumn(name = "definition_id"),
            inverseJoinColumns = @JoinColumn(name = "dictionary_id")
    )
    private final Set<Dictionary> dictionaries = new HashSet<>();

    public Definition() {
    }

    public Definition(String text) {
        this.text = text;
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

    public Set<Word> getWords() {
        return words;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
    }

    public void addWord(Word word) {
        this.words.add(word);
    }

    public Set<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public void removeDictionary(Dictionary dictionary) {
        getDictionaries().remove(dictionary);
        dictionary.getDefinitions().remove(this);
    }

    public void addDictionary(Dictionary dictionary){
        getDictionaries().add(dictionary);
        dictionary.addDefinition(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Definition that = (Definition) o;
        return Objects.equals(id, that.id) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}
