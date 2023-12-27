package com.example.dictionary.application.util;

import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.entity.Example;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.service.CategoryService;
import com.example.dictionary.domain.service.DefinitionService;
import com.example.dictionary.domain.service.ExampleService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class WordEntityAssociationUtil {

    private final CategoryService categoryService;

    private final DefinitionService definitionService;

    private final ExampleService exampleService;

    public WordEntityAssociationUtil(CategoryService categoryService,
                                     DefinitionService definitionService,
                                     ExampleService exampleService) {
        this.categoryService = categoryService;
        this.definitionService = definitionService;
        this.exampleService = exampleService;
    }

    public void associateWordWithEntities(Word word) {
        addToCategory(word);
        addToDefinitions(word);
        addToExamples(word);
    }

    private void addToDefinitions(Word word) {
        Set<Definition> definitions = word.getDefinitions();

        definitions.forEach(
                definition ->
                        definitionService
                                .getDefinitionByText(definition.getText())
                                .ifPresent(value -> {
                                    word.removeDefinition(definition);
                                    word.addDefinition(value);
                                })
        );
    }

    private void addToCategory(Word word) {
        categoryService
                .getCategoryByName(word.getCategory().getName())
                .ifPresent(word::setCategory);
    }

    private void addToExamples(Word word) {
        Set<Example> examples = word.getExamples();

        examples.forEach(
                example ->
                        exampleService
                                .getExampleByText(example.getText())
                                .ifPresent(value -> {
                                    word.removeExample(example);
                                    word.addExample(value);
                                })
        );
    }
}
