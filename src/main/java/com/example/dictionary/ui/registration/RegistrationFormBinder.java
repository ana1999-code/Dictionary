package com.example.dictionary.ui.registration;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.exception.InvalidPasswordException;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.validator.PasswordValidator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.Collection;
import java.util.Objects;

import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;

public class RegistrationFormBinder {

    private final RegistrationForm registrationForm;

    private boolean enablePasswordValidation;


    public RegistrationFormBinder(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    public void addBindingAndValidation(UserFacade userFacade) {
        BeanValidationBinder<UserDto> binder = new BeanValidationBinder<>(UserDto.class);
        binder.bindInstanceFields(registrationForm);

        binder.forField(registrationForm.getPassword())
                .withValidator(this::passwordValidator).bind("password");

        registrationForm.getConfirmPassword()
                .addValueChangeListener(event -> {
                    enablePasswordValidation = true;
                    binder.validate();
                });

        binder.setStatusLabel(registrationForm.getErrorMessageField());

        Button register = registrationForm.getRegister();
        register.addClickListener(event -> {
            try {
                UserDto userDto = new UserDto();
                binder.writeBean(userDto);

                userFacade.registerUser(userDto);

                showSuccess("Successful Registration");
                navigate(register);
            } catch (RuntimeException | ValidationException exception) {
                showNotification(exception.getMessage());
            }
        });

        Button login = registrationForm.getLogin();
        login.addClickListener(event -> navigate(login));
    }

    private static void navigate(Button button) {
        button.getUI().ifPresent(
                ui -> ui.navigate("login")
        );
    }

    private ValidationResult passwordValidator(String password, ValueContext context) {
        try {
            PasswordValidator.validate(password);
        } catch (InvalidPasswordException exception) {
            Collection<String> values = exception.getErrorMap().values();
            StringBuilder message = new StringBuilder();
            for (String error : values) {
                message.append(error).append("\n");
            }
            return ValidationResult.error(Objects.requireNonNull(message).toString());
        }

        if (!enablePasswordValidation) {
            enablePasswordValidation = true;
            return ValidationResult.ok();
        }

        String confirmPassword = registrationForm.getConfirmPassword().getValue();

        if (password != null && password.equals(confirmPassword)) {
            return ValidationResult.ok();
        }

        return ValidationResult.error("Password do not match");
    }
}
