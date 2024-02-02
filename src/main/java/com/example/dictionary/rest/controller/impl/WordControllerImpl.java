package com.example.dictionary.rest.controller.impl;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.rest.controller.WordController;
import jakarta.validation.Valid;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.get.words}")
@Profile("rest")
public class WordControllerImpl implements WordController {

    private final WordFacade wordFacade;

    public WordControllerImpl(WordFacade wordFacade) {
        this.wordFacade = wordFacade;
    }

    @Override
    @GetMapping("all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public ResponseEntity<List<WordDto>> getAllWords() {
        List<WordDto> words = wordFacade.getAllWords();
        return new ResponseEntity<>(words, OK);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public ResponseEntity<List<WordDto>> getAllWords(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "addedAt", required = false) String sortBy) {
        return new ResponseEntity<>(wordFacade.getAllWords(page, pageSize, Sort.by(Sort.Direction.DESC, sortBy)), OK);
    }

    @Override
    @GetMapping("{name}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public ResponseEntity<WordDto> getWordByName(@PathVariable("name") String name) {
        WordDto word = wordFacade.getWordByName(name);
        return new ResponseEntity<>(word, OK);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('word:write')")
    public ResponseEntity<WordDto> addWord(@RequestBody @Valid WordDto wordDto) {
        WordDto addedWord = wordFacade.addWord(wordDto);
        return new ResponseEntity<>(addedWord, CREATED);
    }

    @Override
    @DeleteMapping("{name}")
    @ResponseStatus(value = NO_CONTENT)
    @PreAuthorize("hasAuthority('word:write')")
    public void deleteWordByName(@PathVariable("name") String name) {
        wordFacade.deleteWordByName(name);
    }

    @Override
    @PutMapping("{name}/definitions")
    @PreAuthorize("hasAuthority('word:write')")
    public ResponseEntity<WordDto> addDefinitionToWord(@PathVariable("name") String name,
                                                       @RequestBody @Valid DefinitionDto definitionDto) {
        WordDto updatedWord = wordFacade.addDefinitionToWord(name, definitionDto);
        return new ResponseEntity<>(updatedWord, OK);
    }

    @Override
    @DeleteMapping("{name}/definitions")
    @PreAuthorize("hasAuthority('word:write')")
    public ResponseEntity<WordDto> removeDefinitionFromWord(@PathVariable("name") String name,
                                                            @RequestBody @Valid DefinitionDto definitionDto) {
        WordDto updatedWord = wordFacade.removeDefinitionFromWord(name, definitionDto);

        return new ResponseEntity<>(updatedWord, OK);
    }

    @Override
    @PutMapping("{name}/examples")
    @PreAuthorize("hasAuthority('word:write')")
    public ResponseEntity<WordDto> addExampleToWord(@PathVariable("name") String name,
                                                    @RequestBody @Valid ExampleDto exampleDto) {
        WordDto updatedWord = wordFacade.addExampleToWord(name, exampleDto);

        return new ResponseEntity<>(updatedWord, OK);
    }

    @Override
    @DeleteMapping("{name}/examples")
    @PreAuthorize("hasAuthority('word:write')")
    public ResponseEntity<WordDto> removeExampleFromWord(@PathVariable("name") String name,
                                                         @RequestBody @Valid ExampleDto exampleDto) {
        WordDto updatedWord = wordFacade.removeExampleFromWord(name, exampleDto);
        return new ResponseEntity<>(updatedWord, OK);
    }

    @Override
    @GetMapping("{name}/synonyms")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public ResponseEntity<Set<WordDto>> getAllSynonyms(@PathVariable String name) {
        Set<WordDto> synonyms = wordFacade.getAllSynonyms(name);
        return new ResponseEntity<>(synonyms, OK);
    }

    @Override
    @GetMapping("{name}/antonyms")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public ResponseEntity<Set<WordDto>> getAllAntonyms(@PathVariable String name) {
        Set<WordDto> antonyms = wordFacade.getAllAntonyms(name);
        return new ResponseEntity<>(antonyms, OK);
    }

    @Override
    @PutMapping("{name}/synonyms")
    @ResponseStatus(value = CREATED)
    @PreAuthorize("hasAuthority('word:write')")
    public void addSynonym(@PathVariable("name") String name,
                           @RequestBody WordDto synonym) {
        wordFacade.addSynonym(name, synonym);
    }

    @Override
    @DeleteMapping("{name}/synonyms")
    @ResponseStatus(value = NO_CONTENT)
    @PreAuthorize("hasAuthority('word:write')")
    public void removeSynonym(@PathVariable("name") String name,
                              @RequestBody WordDto synonym) {
        wordFacade.removeSynonym(name, synonym);
    }

    @Override
    @PutMapping("{name}/antonyms")
    @ResponseStatus(value = CREATED)
    @PreAuthorize("hasAuthority('word:write')")
    public void addAntonym(@PathVariable("name") String name,
                           @RequestBody WordDto antonym) {
        wordFacade.addAntonym(name, antonym);
    }

    @Override
    @DeleteMapping("{name}/antonyms")
    @ResponseStatus(value = NO_CONTENT)
    @PreAuthorize("hasAuthority('word:write')")
    public void removeAntonym(@PathVariable("name") String name,
                              @RequestBody WordDto antonym) {
        wordFacade.removeAntonym(name, antonym);
    }

    @Override
    @PutMapping("{name}/comments")
    @ResponseStatus(value = CREATED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public void addComment(@PathVariable("name") String name,
                           @RequestBody @Valid CommentDto commentDto) {
        wordFacade.addComment(name, commentDto);
    }

    @Override
    @DeleteMapping("{name}/comments")
    @ResponseStatus(value = NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_LEARNER', 'ROLE_EDITOR')")
    public void removeComment(@PathVariable String name,
                              @RequestParam("commentId") Integer commentId) {
        wordFacade.removeComment(name, commentId);
    }

    @Override
    @GetMapping("/report/contributions")
    @ResponseStatus(value = CREATED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void generateWordsContributionReport() throws
            DRException,
            IOException,
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        wordFacade.generateWordsContributionReport();
    }

    @Override
    @GetMapping("/report/statistics")
    @ResponseStatus(value = CREATED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void generateWordsStatisticsReport(@RequestParam("year") Integer year,
                                              @RequestParam("month") String month) throws
            DRException,
            IOException,
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        wordFacade.generateWordsStatisticsReport(year, month);
    }
}
