package com.example.dictionary.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.DATE;

@Entity
@Table(name = "words")
public class Word {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(insertable = false, updatable = false)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(cascade = PERSIST, fetch = LAZY)
    @JoinTable(name = "word_definition",
            joinColumns = @JoinColumn(name = "word_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "definition_id", referencedColumnName = "id"))
    private final Set<Definition> definitions = new HashSet<>();

    @ManyToMany(cascade = PERSIST, fetch = LAZY)
    @JoinTable(name = "word_example",
            joinColumns = @JoinColumn(name = "word_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "example_id", referencedColumnName = "id"))
    private final Set<Example> examples = new HashSet<>();

    @ManyToMany(cascade = {PERSIST, MERGE, REMOVE}, fetch = LAZY)
    @JoinTable(name = "word_synonyms",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "synonym_id"))
    private final Set<Word> synonyms = new HashSet<>();

    @ManyToMany(cascade = {PERSIST, MERGE}, fetch = LAZY)
    @JoinTable(name = "word_antonyms",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "antonym_id"))
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    private final Set<Word> antonyms = new HashSet<>();

    @ManyToOne(fetch = LAZY, cascade = {PERSIST, MERGE})
    @JoinColumn(nullable = false)
    private Category category;

    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "word_contributors",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private final Set<User> contributors = new HashSet<>();

    @OneToMany(mappedBy = "word", orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(value = DATE)
    private LocalDate addedAt;

    public Word() {
    }

    public Word(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Definition> getDefinitions() {
        return definitions;
    }

    public Set<Example> getExamples() {
        return examples;
    }

    public Set<Word> getSynonyms() {
        return synonyms;
    }

    public Set<Word> getAntonyms() {
        return antonyms;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<User> getContributors() {
        return contributors;
    }

    public LocalDate getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDate addedAt) {
        this.addedAt = addedAt;
    }

    public void addDefinition(Definition definition) {
        definitions.add(definition);
        definition.addWord(this);
    }

    public void removeDefinition(Definition definition) {
        definitions.remove(definition);
        definition.getWords().remove(this);
    }

    public void addSynonym(Word synonym) {
        if (!synonyms.contains(synonym)) {
            synonyms.add(synonym);
            synonym.addSynonym(this);
        }
    }

    public void removeSynonym(Word synonym) {
        if (synonyms.contains(synonym)) {
            synonyms.remove(synonym);
            synonym.removeSynonym(this);
        }
    }

    public void addAntonym(Word antonym) {
        if (!antonyms.contains(antonym)) {
            antonyms.add(antonym);
            antonym.addAntonym(this);
        }
    }

    public void removeAntonym(Word antonym) {
        if (antonyms.contains(antonym)) {
            antonyms.remove(antonym);
            antonym.removeAntonym(this);
        }
    }

    public void addExample(Example example) {
        examples.add(example);
        example.addWord(this);
    }

    public void removeExample(Example example) {
        examples.remove(example);
        example.getWords().remove(this);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setWord(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setWord(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(id, word.id)
                && Objects.equals(name, word.name)
                && Objects.equals(category, word.category)
                && Objects.equals(contributors, word.contributors)
                && Objects.equals(comments, word.comments)
                && Objects.equals(addedAt, word.addedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, contributors, comments, addedAt);
    }
}
