package com.example.dictionary.domain.service.impl;

import com.example.dictionary.application.annotation.ContributeByUser;
import com.example.dictionary.application.report.data.WordDetail;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.repository.WordRepository;
import com.example.dictionary.domain.service.WordService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.dictionary.domain.cache.CacheContext.WORDS_CACHE;
import static com.example.dictionary.domain.cache.CacheContext.WORDS_DETAILS_CACHE;
import static com.example.dictionary.domain.cache.CacheContext.WORD_CACHE;

@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    @Transactional
    @Cacheable(value = WORDS_CACHE, keyGenerator = "cacheKeyGenerator")
    public List<Word> getAllWords() {
        return wordRepository.findAll(Sort.by(Sort.Direction.DESC, "addedAt"));
    }

    @Override
    @Cacheable(value = WORDS_CACHE, key = "#page + #pageSize")
    public List<Word> getAllWords(int page, int pageSize) {
        return wordRepository.findAll(PageRequest.of(page, pageSize,
                Sort.by(Sort.Direction.DESC, "addedAt"))).toList();
    }

    @Override
    @Cacheable(value = WORD_CACHE, key = "#name")
    public Optional<Word> getWordByName(String name) {
        return wordRepository.findByName(name);
    }

    @Override
    @ContributeByUser
    @CacheEvict(value = {WORDS_CACHE, WORDS_DETAILS_CACHE, WORD_CACHE}, allEntries = true)
    @CachePut(value = WORD_CACHE, key = "#result.name")
    public Word addWord(Word word) {
        return wordRepository.save(word);
    }

    @Override
    public boolean existsWordByName(String name) {
        return wordRepository.existsByName(name);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = {WORDS_CACHE, WORDS_DETAILS_CACHE}, allEntries = true),
            @CacheEvict(value = WORD_CACHE, key = "#name")
    })
    public void deleteWordByName(String name) {
        wordRepository.deleteByName(name);
    }

    @Override
    @Cacheable(value = WORDS_DETAILS_CACHE, keyGenerator = "cacheKeyGenerator")
    public List<WordDetail> getWordsDetails() {
        return wordRepository.getWordsDetails();
    }

    @Override
    @Cacheable(value = WORD_CACHE, key = "#name")
    public Optional<Word> getWordByNameWithContributors(String name) {
        return wordRepository.findByNameWithContributors(name);
    }
}
