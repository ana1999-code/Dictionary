package com.example.dictionary.ui.profile;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;

import java.util.Arrays;

import static com.example.dictionary.ui.util.UiUtils.APP_COLOR;
import static com.example.dictionary.ui.util.UiUtils.IMAGE_FILE_TYPES;
import static com.example.dictionary.ui.util.UiUtils.getCloseButton;


public class ProfileForm extends HorizontalLayout {

    private Image image;

    private TextField firstName;

    private TextField lastName;

    private TextField email;

    private Upload upload;

    private Button edit;

    private Button save;

    private Button cancel;

    private VerticalLayout userCredentialsLayout;

    public ProfileForm() {
        image = new Image("images/img.png", "Profile Image");
        image.setHeight("19em");
        image.setWidth("18em");

        Button uploadButton = new Button("Upload Profile Image...");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        uploadButton.getStyle().set("padding", "0 4em");

        upload = new Upload();
        upload.setAcceptedFileTypes(IMAGE_FILE_TYPES);
        upload.setUploadButton(uploadButton);
        upload.setDropAllowed(false);
        upload.getStyle()
                .set("border", "1px dashed " + APP_COLOR)
                .set("border-radius", "5px");

        firstName = new TextField("First Name");
        firstName.setWidth("100%");

        lastName = new TextField("Last Name");
        lastName.setWidth("100%");

        email = new TextField("Email");
        email.setWidth("100%");

        edit = new Button("Edit profile");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save = new Button();
        save.setIcon(new Icon(VaadinIcon.CHECK));
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        cancel = getCloseButton();

        setReadOnly(true, email);
        setCredentialFieldsReadOnly(true);
        VerticalLayout imageLayout = new VerticalLayout(image, upload);
        imageLayout.setMargin(true);

        userCredentialsLayout = new VerticalLayout(firstName, lastName, email, edit);
        userCredentialsLayout.setPadding(false);
        userCredentialsLayout.setMargin(true);

        imageLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        userCredentialsLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        getStyle().set("padding", "var(--lumo-space-l)");
        setWidth("50%");
        add(imageLayout, userCredentialsLayout);
    }

    public Image getImage() {
        return image;
    }

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField email) {
        this.email = email;
    }

    public Upload getUpload() {
        return upload;
    }

    public Button getEdit() {
        return edit;
    }

    public Button getSave() {
        return save;
    }

    public Button getCancel() {
        return cancel;
    }

    public VerticalLayout getUserCredentialsLayout() {
        return userCredentialsLayout;
    }

    private void setReadOnly(boolean isReadOnly, HasValueAndElement<?, ?>... components) {
        Arrays.stream(components)
                .forEach(component -> component.setReadOnly(isReadOnly));
    }

    public void setCredentialFieldsReadOnly(boolean isReadOnly) {
        setReadOnly(isReadOnly, firstName, lastName);
    }
}
