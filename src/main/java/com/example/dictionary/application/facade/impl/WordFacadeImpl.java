package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.DuplicateResourceException;
import com.example.dictionary.application.exception.IllegalOperationException;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.application.mapper.CommentMapper;
import com.example.dictionary.application.mapper.DefinitionMapper;
import com.example.dictionary.application.mapper.ExampleMapper;
import com.example.dictionary.application.mapper.WordMapper;
import com.example.dictionary.application.report.WordsContributionReportGenerator;
import com.example.dictionary.application.report.WordsStatisticReportGenerator;
import com.example.dictionary.application.report.data.WordDetail;
import com.example.dictionary.application.security.util.SecurityUtils;
import com.example.dictionary.application.util.WordEntityAssociationUtil;
import com.example.dictionary.application.validator.ExampleValidator;
import com.example.dictionary.application.validator.WordValidator;
import com.example.dictionary.domain.entity.Comment;
import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.entity.Example;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.service.CommentService;
import com.example.dictionary.domain.service.DefinitionService;
import com.example.dictionary.domain.service.ExampleService;
import com.example.dictionary.domain.service.UserService;
import com.example.dictionary.domain.service.WordService;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.dictionary.domain.cache.CacheContext.WORDS_CACHE;
import static com.example.dictionary.domain.cache.CacheContext.WORDS_DETAILS_CACHE;
import static com.example.dictionary.domain.cache.CacheContext.WORD_CACHE;

@Component
public class WordFacadeImpl implements WordFacade {

    private final WordService wordService;

    private final WordMapper wordMapper;

    private final WordValidator validator;

    private final DefinitionService definitionService;

    private final ExampleService exampleService;

    private final UserService userService;

    private final CommentService commentService;

    private final DefinitionMapper definitionMapper;

    private final ExampleMapper exampleMapper;

    private final CommentMapper commentMapper;

    private final WordEntityAssociationUtil associationUtil;

    private final JobLauncher jobLauncher;

    @Qualifier("importWordsFromCsvToDbJob")
    private final Job importWordsFromCsvToDbJob;

    @Qualifier("openFileLocationJop")
    private final Job openFileLocationJop;

    public WordFacadeImpl(WordService wordService,
                          WordMapper wordMapper,
                          WordValidator validator,
                          DefinitionService definitionService,
                          ExampleService exampleService,
                          UserService userService,
                          CommentService commentService,
                          DefinitionMapper definitionMapper,
                          ExampleMapper exampleMapper,
                          CommentMapper commentMapper,
                          WordEntityAssociationUtil associationUtil,
                          JobLauncher jobLauncher,
                          Job importWordsFromCsvToDbJob,
                          Job openFileLocationJop) {
        this.wordService = wordService;
        this.wordMapper = wordMapper;
        this.validator = validator;
        this.definitionService = definitionService;
        this.exampleService = exampleService;
        this.userService = userService;
        this.commentService = commentService;
        this.definitionMapper = definitionMapper;
        this.exampleMapper = exampleMapper;
        this.commentMapper = commentMapper;
        this.associationUtil = associationUtil;
        this.jobLauncher = jobLauncher;
        this.importWordsFromCsvToDbJob = importWordsFromCsvToDbJob;
        this.openFileLocationJop = openFileLocationJop;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<WordDto> getAllWords() {
        List<Word> allWords = wordService.getAllWords();
        return allWords.stream()
                .map(wordMapper::wordToWordDto)
                .toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public WordDto getWordByName(String name) {
        Word word = getWord(name);

        return wordMapper.wordToWordDto(word);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public WordDto addWord(WordDto wordDto) {
        validator.validate(wordDto);

        Word word = wordMapper.wordDtoToWord(wordDto);
        associationUtil.associateWordWithEntities(word);

        word.setAddedAt(LocalDateTime.now());
        Word addedWord = wordService.addWord(word);

        return wordMapper.wordToWordDto(addedWord);
    }

    @Override
    public void deleteWordByName(String name) {
        getWord(name);

        wordService.deleteWordByName(name);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public WordDto addDefinitionToWord(String name, DefinitionDto definitionDto) {
        Word word = getWordWithContributors(name);

        Definition definition = getOrCreateDefinition(definitionDto);
        verifyDefinitionIsNotPresent(word, definition);

        word.addDefinition(definition);
        Word addedWord = wordService.addWord(word);

        return wordMapper.wordToWordDto(addedWord);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public WordDto removeDefinitionFromWord(String name, DefinitionDto definitionDto) {
        Definition definition = definitionMapper.definitionDtoToDefinition(definitionDto);
        Word word = getWordWithContributors(name);

        Definition definitionToDelete = getDefinitionToDelete(definition, word);

        word.removeDefinition(definitionToDelete);
        Word updatedWord = wordService.addWord(word);

        return wordMapper.wordToWordDto(updatedWord);
    }

    @Override
    public WordDto addExampleToWord(String name, ExampleDto exampleDto) {
        Word word = getWordWithContributors(name);
        Example example = getOrCreateExample(exampleDto);

        verifyExampleIsValid(word, example);

        word.addExample(example);
        Word updatedWord = wordService.addWord(word);
        return wordMapper.wordToWordDto(updatedWord);
    }

    @Override
    public WordDto removeExampleFromWord(String name, ExampleDto exampleDto) {
        Example example = exampleMapper.exampleDtoToExample(exampleDto);
        Word word = getWordWithContributors(name);

        Example exampleToDelete = getExampleToDelete(example, word);

        word.removeExample(exampleToDelete);
        Word updatedWord = wordService.addWord(word);
        return wordMapper.wordToWordDto(updatedWord);
    }

    @Override
    public Set<WordDto> getAllSynonyms(String name) {
        Word word = getWord(name);
        Set<Word> synonyms = word.getSynonyms();

        return synonyms.stream()
                .map(wordMapper::wordToWordDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<WordDto> getAllAntonyms(String name) {
        Word word = getWord(name);
        Set<Word> antonyms = word.getAntonyms();

        return antonyms.stream()
                .map(wordMapper::wordToWordDto)
                .collect(Collectors.toSet());
    }

    @Override
    public void addSynonym(String name, WordDto synonym) {
        Word word = getWordWithContributors(name);
        Word synonymToAdd = getWordWithContributors(synonym.getName());

        verifyWordIsNotPresent(word, synonymToAdd);

        word.addSynonym(synonymToAdd);
    }

    @Override
    public void removeSynonym(String name, WordDto synonym) {
        Word word = getWordWithContributors(name);
        Word synonymToRemove = getWordWithContributors(synonym.getName());

        verifyWordIsPresent(synonymToRemove, word.getSynonyms());

        word.removeSynonym(synonymToRemove);
    }

    @Override
    public void addAntonym(String name, WordDto antonym) {
        Word word = getWordWithContributors(name);
        Word antonymToAdd = getWordWithContributors(antonym.getName());

        verifyWordIsNotPresent(word, antonymToAdd);

        word.addAntonym(antonymToAdd);
    }

    @Override
    public void removeAntonym(String name, WordDto antonym) {
        Word word = getWordWithContributors(name);
        Word antonymToRemove = getWordWithContributors(antonym.getName());

        verifyWordIsPresent(antonymToRemove, word.getAntonyms());

        word.removeAntonym(antonymToRemove);
    }

    @Override
    @CacheEvict(value = {WORDS_CACHE, WORDS_DETAILS_CACHE, WORD_CACHE}, allEntries = true)
    @CachePut(value = WORD_CACHE, key = "#name")
    public void addComment(String name, CommentDto commentDto) {
        Word word = getWord(name);

        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setCommenter(userService.findByEmail(getCurrentUser()).get());

        word.addComment(comment);
        commentService.addComment(comment);
    }

    @Override
    @CacheEvict(value = {WORDS_CACHE, WORDS_DETAILS_CACHE, WORD_CACHE}, allEntries = true)
    @CachePut(value = WORD_CACHE, key = "#name")
    public void removeComment(String name, Integer commentId) {
        Word word = getWord(name);

        Comment comment = commentService.getCommentById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("No comment found"));

        word.removeComment(comment);
        commentService.removeComment(comment);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CommentDto> getAllCommentsByWord(String name) {
        List<Comment> commentsByWord = commentService.getAllCommentsByWord(name);
        return commentsByWord.stream()
                .map(commentMapper::commentToCommentDto)
                .toList();
    }

    @Override
    @CacheEvict(value = WORDS_CACHE, allEntries = true)
    public void uploadFile(String path, String fileName, String fileLocation) throws
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        JobParameters params = new JobParametersBuilder()
                .addString("filePath", path)
                .addString("fileName", fileName)
                .addString("fileLocation", fileLocation)
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(importWordsFromCsvToDbJob, params);
    }

    @Override
    public void generateWordsContributionReport() throws
            DRException,
            IOException,
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        List<WordDetail> wordDetails = wordService.getWordsDetails();
        WordsContributionReportGenerator reportGenerator =
                new WordsContributionReportGenerator(wordDetails, getCurrentUser());
        reportGenerator.generate();
        openReportLocation(reportGenerator.getPath());
    }

    @Override
    public void generateWordsStatisticsReport(Integer year, String month) throws
            DRException,
            IOException,
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        List<Word> words = wordService.getAllWords();
        WordsStatisticReportGenerator reportGenerator =
                new WordsStatisticReportGenerator(words, getCurrentUser());

        reportGenerator.setYear(year);
        reportGenerator.setMonth(Month.valueOf(month));
        reportGenerator.generate();
        openReportLocation(reportGenerator.getPath());
    }

    @Override
    public List<WordDetail> getAllWordsDetails() {
        return wordService.getWordsDetails();
    }

    private void openReportLocation(String path) throws
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        JobParameters params = new JobParametersBuilder()
                .addString("filePath", path)
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(openFileLocationJop, params);
    }

    private String getCurrentUser() {
        return SecurityUtils.getUsername();
    }

    private Word getWord(String name) {
        return wordService.getWordByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Word [%s] not found".formatted(name))
                );
    }

    private Word getWordWithContributors(String name) {
        return wordService.getWordByNameWithContributors(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Word [%s] not found".formatted(name))
                );
    }

    private Definition getOrCreateDefinition(DefinitionDto definitionDto) {
        Optional<Definition> existingDefinition = definitionService
                .getDefinitionByText(definitionDto.getText());

        return existingDefinition.orElseGet(() -> {
            Definition newDefinition = definitionMapper.definitionDtoToDefinition(definitionDto);
            definitionService.saveDefinition(newDefinition);
            return newDefinition;
        });
    }

    public static void verifyDefinitionIsNotPresent(Word word, Definition definition) {
        if (word.getDefinitions().contains(definition)) {
            throw new DuplicateResourceException(
                    "Definition [%s] is already present in word definitions"
                            .formatted(definition.getText())
            );
        }
    }

    private Definition getDefinitionToDelete(Definition definition, Word word) {
        String text = definition.getText();

        Definition definitionToDelete = definitionService.getDefinitionByText(text)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Definition [%s] not found".formatted(text)));

        if (word.getDefinitions().size() == 1) {
            throw new IllegalOperationException(
                    "Word [%s] has only one definition that is required".formatted(word.getName())
            );
        } else if (!word.getDefinitions().contains(definitionToDelete)) {
            throw new ResourceNotFoundException("Definition [%s] not found for the word [%s]"
                    .formatted(text, word.getName())
            );
        }
        return definitionToDelete;
    }


    private Example getExampleToDelete(Example example, Word word) {
        String text = example.getText();

        Example exampleToDelete = exampleService.getExampleByText(text)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Example [%s] not found".formatted(text)));

        if (!word.getExamples().contains(exampleToDelete)) {
            throw new ResourceNotFoundException(
                    "Example [%s] not found for the word [%s]"
                            .formatted(text, word.getName())
            );
        }

        return exampleToDelete;
    }

    private Example getOrCreateExample(ExampleDto exampleDto) {
        Optional<Example> example = exampleService.getExampleByText(exampleDto.getText());

        return example.orElseGet(() -> {
            Example newExample = exampleMapper.exampleDtoToExample(exampleDto);
            exampleService.saveExample(newExample);
            return newExample;
        });
    }

    private void verifyExampleIsValid(Word word, Example example) {
        ExampleValidator.validate(word.getName(), example.getText());

        if (word.getExamples().contains(example)) {
            throw new DuplicateResourceException(
                    "Example [%s] is already present in word examples"
                            .formatted(example.getText())
            );
        }
    }

    private static void verifyWordIsNotPresent(Word word, Word wordToAdd) {
        if (isWordContaining(word, wordToAdd)) {
            throw new DuplicateResourceException(
                    "Synonym or Antonym [%s] is already linked with word [%s]"
                            .formatted(wordToAdd.getName(), word.getName()));
        }
    }

    private static boolean isWordContaining(Word word, Word linkedWord) {
        return word.getSynonyms().contains(linkedWord)
                && !word.getAntonyms().contains(linkedWord)
                || !word.getSynonyms().contains(linkedWord)
                && word.getAntonyms().contains(linkedWord);
    }

    private static void verifyWordIsPresent(Word wordToRemove, Set<Word> synonyms) {
        if (!synonyms.contains(wordToRemove)) {
            throw new ResourceNotFoundException("Synonym or Antonym [%s] is not present"
                    .formatted(wordToRemove.getName()));
        }
    }
}
