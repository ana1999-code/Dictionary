package com.example.dictionary.ui.util;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.ui.words.common.CommonDialog;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY;
import static com.vaadin.flow.component.icon.VaadinIcon.CLOSE;
import static com.vaadin.flow.component.icon.VaadinIcon.PLUS;
import static com.vaadin.flow.component.icon.VaadinIcon.SEARCH;
import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS;
import static com.vaadin.flow.data.value.ValueChangeMode.EAGER;

public class UiUtils {

    public static final String APP_NAME = "WordCraft";

    public static final String LOGOUT_SUCCESS_URL = "/api/dictionary/login";

    public static final String APP_COLOR = "var(--_lumo-button-primary-background-color, var(--lumo-primary-color))";

    public static final String DD_MM_YYYY = "dd-MM-yyyy";

    public static final String CSV = "text/csv";

    public static final String JSON = "application/json";

    public static final String[] IMAGE_FILE_TYPES = {".png", ".jpeg", ".jpg"};

    public static final String FILE_LOCATION = "src/main/resources/data/";

    public static final String PROCESSED = "PROCESSED_";

    public static final String WIDTH = "50%";

    public static void showNotification(String message) {
        Notification notification = new Notification(message, 5000, TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    public static void showSuccess(String message) {
        Notification notification =
                Notification.show(message);

        notification.addThemeVariants(LUMO_SUCCESS);
    }

    public static Button getCloseButton() {
        Button close = new Button();
        close.setIcon(new Icon(CLOSE));
        close.addThemeVariants(ButtonVariant.LUMO_ERROR, LUMO_TERTIARY, LUMO_SMALL);

        return close;
    }

    public static TextField getConfiguredSearchField(String placeholder) {
        TextField searchField = new TextField();
        searchField.setPlaceholder(placeholder);
        searchField.setValueChangeMode(EAGER);
        searchField.setSuffixComponent(new Icon(SEARCH));
        searchField.setWidthFull();
        return searchField;
    }

    public static Avatar getAvatar(UserDto userDto) {
        String name = userDto.getFirstName() + " " + userDto.getLastName();
        Avatar avatar = new Avatar();

        byte[] profileImage = userDto.getProfileImage();
        if (profileImage != null) {
            StreamResource resource = new StreamResource("image",
                    () -> new ByteArrayInputStream(profileImage));
            avatar.setImageResource(resource);
        } else {
            avatar.setName(name);
            avatar.setColorIndex(name.length() % 7);
        }
        return avatar;
    }

    public static Button getAddButton() {
        Button button = new Button(new Icon(PLUS));
        button.addThemeVariants(LUMO_TERTIARY);
        return button;
    }

    public static Button getSaveButton(CommonDialog dialog) {
        Button saveButton = dialog.getSecondRightButton();
        saveButton.setText(dialog.getTranslation("save"));
        saveButton.addThemeVariants(LUMO_PRIMARY);
        return saveButton;
    }

    public static Button getCancelButton(CommonDialog dialog) {
        Button cancelButton = dialog.getLeftButton();
        cancelButton.setText(dialog.getTranslation("cancel"));
        cancelButton.addThemeVariants(LUMO_ERROR);
        return cancelButton;
    }

    public static Button getResetButton(CommonDialog dialog) {
        Button resetButton = dialog.getFirstRightButton();
        resetButton.setText(dialog.getTranslation("reset"));
        resetButton.addThemeVariants(LUMO_TERTIARY);
        return resetButton;
    }
}
