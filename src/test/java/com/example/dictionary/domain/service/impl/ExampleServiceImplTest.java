package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Example;
import com.example.dictionary.domain.repository.ExampleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.dictionary.utils.TestUtils.EXAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExampleServiceImplTest {

    @Mock
    private ExampleRepository exampleRepository;

    @InjectMocks
    private ExampleServiceImpl exampleService;

    @Test
    void testGetExampleByText() {
        when(exampleRepository.findExampleByText(anyString())).thenReturn(Optional.of(EXAMPLE));

        Optional<Example> exampleByText = exampleService.getExampleByText(EXAMPLE.getText());

        assertEquals(EXAMPLE, exampleByText.get());
    }
}