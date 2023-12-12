package com.example.dictionary.rest.controller;

import com.example.dictionary.application.dto.WordDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WordController {

    ResponseEntity<List<WordDto>> getAllWords();
}
