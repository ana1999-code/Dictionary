package com.example.dictionary.ui.registration;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Arrays;

import static com.example.dictionary.ui.util.UiUtils.APP_COLOR;
import static com.example.dictionary.ui.util.UiUtils.APP_NAME;

public class RegistrationForm extends FormLayout {

    private H2 title;

    private TextField firstName;

    private TextField lastName;

    private EmailField email;

    private PasswordField password;

    private PasswordField confirmPassword;

    private TextField key;

    private Button register;

    private Span errorMessageField;

    private Button login;

    public RegistrationForm() {
        title = new H2(APP_NAME + " Registration");
        title.getStyle()
                .set("color", "var(--_lumo-button-primary-color, var(--lumo-primary-contrast-color))");

        Div titleSpace = new Div(title);
        titleSpace.getStyle()
                .set("background-color", APP_COLOR)
                .set("padding", "40px 0")
                .set("text-align", "center")
                .set("border-radius", "5px");

        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new EmailField("Email");
        password = new PasswordField("Password");
        confirmPassword = new PasswordField("Confirm Password");
        key = new TextField("Access Key");
        login = new Button("Login");

        setRequiredIndicatorVisible(
                firstName,
                lastName,
                email,
                password,
                confirmPassword,
                key
        );

        errorMessageField = new Span();

        register = new Button("Register");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        login.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        add(
                titleSpace,
                firstName,
                lastName,
                email,
                password,
                confirmPassword,
                key,
                register,
                login
        );

        setResponsiveSteps(
                new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
                new ResponsiveStep("490px", 2, ResponsiveStep.LabelsPosition.TOP));

        setMaxWidth("500px");
        setColspan(titleSpace, 2);
        setColspan(email, 2);
        setColspan(key, 2);
        setColspan(errorMessageField, 2);
        setColspan(register, 2);
        setColspan(login, 2);
    }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Arrays.stream(components)
                .forEach(component -> component.setRequiredIndicatorVisible(true));
    }

    public PasswordField getPassword() {
        return password;
    }

    public PasswordField getConfirmPassword() {
        return confirmPassword;
    }

    public Span getErrorMessageField() {
        return errorMessageField;
    }

    public Button getRegister() {
        return register;
    }

    public Button getLogin() {
        return login;
    }

    public EmailField getEmail() {
        return email;
    }
}
