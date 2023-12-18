package com.example.dictionary.utils;

import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.domain.entity.Category;
import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.entity.Example;
import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.entity.Word;

import java.util.Map;

public class TestUtils {

    public static final CategoryDto CATEGORY_DTO = new CategoryDto("test_category");

    public static final Category CATEGORY = new Category("test_category");

    public static final Definition EXISTING_DEFINITION_FOR_WORD =
            new Definition("Existing definition");

    public static final Definition NON_EXISTING_DEFINITION_FOR_WORD =
            new Definition("Non Existing definition");

    public static final DefinitionDto NON_EXISTING_DEFINITION_DTO_FOR_WORD =
            new DefinitionDto("Non Existing definition");

    public static final DefinitionDto EXISTING_DEFINITION_DTO_FOR_WORD =
            new DefinitionDto("Existing definition");

    public static final WordDto WORD_DTO = new WordDto("test", CATEGORY_DTO);

    public static final Word WORD = new Word("test", CATEGORY);

    public static final String WORD_NOT_FOUND = "Word %s not found".formatted(WORD.getName());

    public static final DefinitionDto DEFINITION_DTO = new DefinitionDto("Test definition");

    public static final String DUPLICATE_WORD = "Word %s already exists".formatted(WORD.getName());

    public static final String DEFINITION_NOT_FOUND_FOR_WORD =
            "Word %s should have at least one definition".formatted(WORD.getName());

    public static final ExampleDto EXAMPLE_DTO_WITHOUT_WORD = new ExampleDto("Example without word");

    public static final ExampleDto EXAMPLE_DTO = new ExampleDto("Test example");

    public static final String EXAMPLE_NOT_CONTAINS_WORD =
            "Provided example does not contain the word %s".formatted(WORD.getName());

    public static final Example EXAMPLE_WITHOUT_WORD = new Example("Example without word");

    public static final Example EXAMPLE = new Example("Test example");

    public static final Definition DEFINITION = new Definition("Definition test");

    public static final String INVALID_WORD = "Word must contain only letters";

    public static final UserDto USER_DTO = new UserDto();

    public static final User USER = new User();

    public static final String EMAIL_IS_TAKEN = "Email %s is already taken";

    public static final String USER_NOT_FOUND = "User with email %s not found";

    public static final String FIRST_NAME_IS_REQUIRED = "First Name is required";

    public static final String LAST_NAME_IS_REQUIRED = "Last Name is required";

    public static final String INCORRECT_EMAIL_FORMAT = "Incorrect email format";

    public static final String PASSWORD_REQUIRED = "Password is required";

    public static final Map<String, String> PASSWORD_VALIDATION_ERRORS = Map.of(
            "length", "Password should have at least 8 chars",
            "upper_letter", "Password should have at least one uppercase char",
            "lower_letter", "Password should have at least one lowercase char",
            "number", "Password should have at least one number",
            "whitespace", "Password should not have any whitespaces",
            "special_char", "Password should have at least one special char");

    public static final String KEY_REQUIRED = "Key is required";

    public static final String INVALID_KEY = "Invalid key";

    public static final String DEFINITION_IS_PRESENT = "Definition [%s] is already present in word definitions"
            .formatted(DEFINITION.getText());

    public static final String DEFINITION_NOT_FOUND = "Definition [%s] not found"
            .formatted(DEFINITION.getText());

    public static final String ONLY_ONE_DEFINITION = "Word %s has only one definition that is required"
            .formatted(WORD.getName());

    public static final String DEFINITION_NOT_FOUND_FOR_THE_WORD = "Definition [%s] not found for the word %s"
            .formatted(NON_EXISTING_DEFINITION_FOR_WORD.getText(), WORD.getName());

    public static final String EXAMPLE_ALREADY_PRESENT = "Example [%s] is already present in word examples"
            .formatted(EXAMPLE.getText());

    public static final String EXAMPLE_NOT_FOUND = "Example [%s] not found".formatted(EXAMPLE.getText());
    public static final String EXAMPLE_NOT_FOUND_FOR_THE_WORD = "Example [%s] not found for the word %s"
            .formatted(EXAMPLE.getText(), WORD.getName());
}
