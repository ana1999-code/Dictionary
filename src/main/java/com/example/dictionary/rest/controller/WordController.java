package com.example.dictionary.rest.controller;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface WordController {

    ResponseEntity<List<WordDto>> getAllWords();

    ResponseEntity<WordDto> getWordByName(String name);

    ResponseEntity<WordDto> addWord(WordDto wordDto);

    void deleteWordByName(String name);

    ResponseEntity<WordDto> addDefinitionToWord(String name, DefinitionDto definitionDto);

    ResponseEntity<WordDto> removeDefinitionFromWord(String name, DefinitionDto definitionDto);

    ResponseEntity<WordDto> addExampleToWord(String name, ExampleDto exampleDto);

    ResponseEntity<WordDto> removeExampleFromWord(String name, ExampleDto exampleDto);

    ResponseEntity<Set<WordDto>> getAllSynonyms(String name);

    ResponseEntity<Set<WordDto>> getAllAntonyms(String name);

    void addSynonym(String name, WordDto synonym);

    void removeSynonym(String name, WordDto synonym);

    void addAntonym(String name, WordDto antonym);

    void removeAntonym(String name, WordDto antonym);

    void addComment(String name, CommentDto commentDto);

    void removeComment(String name, CommentDto commentDto);

    void generateWordsContributionReport() throws
            DRException,
            IOException,
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException;

    void generateWordsStatisticsReport(Integer year, String month) throws
            DRException,
            IOException,
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException;
}
