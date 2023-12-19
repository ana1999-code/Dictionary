package com.example.dictionary.ui.login;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.facade.UserFacade;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("register")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

    BeanValidationBinder<UserDto> userBinder = new BeanValidationBinder<>(UserDto.class);

    private final UserFacade userFacade;

    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    TextField email = new TextField("Email");
    PasswordField password = new PasswordField("Password");
    PasswordField passwordConfirmation = new PasswordField("Password confirmation");
    TextField key = new TextField("Access key");

    private UserDto userDto = new UserDto();

    public RegistrationView(UserFacade userFacade) {
        this.userFacade = userFacade;

        userBinder.bindInstanceFields(this);

        Button register = new Button("Register");
        register.addClickListener(event -> {
            try {
                userBinder.writeBean(userDto);
                userFacade.registerUser(userDto);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });

        add(
                firstName,
                lastName,
                email,
                password,
                passwordConfirmation,
                key,
                register
        );
    }

}
