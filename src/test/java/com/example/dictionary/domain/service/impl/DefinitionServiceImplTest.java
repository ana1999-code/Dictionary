package com.example.dictionary.domain.service.impl;

import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.repository.DefinitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.dictionary.utils.TestUtils.DEFINITION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefinitionServiceImplTest {

    @Mock
    private DefinitionRepository definitionRepository;

    @InjectMocks
    private DefinitionServiceImpl definitionService;

    @Test
    void testGetDefinitionByText() {
        when(definitionRepository.getDefinitionByText(anyString())).thenReturn(Optional.of(DEFINITION));

        Optional<Definition> definitionByText = definitionService.getDefinitionByText(DEFINITION.getText());

        assertEquals(DEFINITION, definitionByText.get());
    }

    @Test
    void testGetAllDefinitions() {
        when(definitionRepository.findAll()).thenReturn(List.of(DEFINITION));

        List<Definition> definitions = definitionService.getAllDefinitions();

        assertTrue(definitions.contains(DEFINITION));
    }

    @Test
    void testSaveDefinition() {
        when(definitionRepository.save(any(Definition.class))).thenReturn(DEFINITION);

        Definition savedDefinition = definitionService.saveDefinition(DEFINITION);

        assertEquals(DEFINITION, savedDefinition);
    }
}