package com.example.dictionary.ui.registration;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.facade.UserFacade;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class RegistrationFormBinder {

    private final RegistrationForm registrationForm;

    private boolean enablePasswordValidation;


    public RegistrationFormBinder(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    public void addBindingAndValidation(UserFacade userFacade) {
        BeanValidationBinder<UserDto> binder = new BeanValidationBinder<>(UserDto.class);
        binder.bindInstanceFields(registrationForm);

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

                showSuccess(userDto);
                navigate(register);
            } catch (ValidationException e) {
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

    private void showSuccess(UserDto userDto) {
        Notification notification =
                Notification.show("Successfully Registration");

        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
