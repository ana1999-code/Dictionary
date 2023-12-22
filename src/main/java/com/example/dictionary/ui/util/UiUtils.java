package com.example.dictionary.ui.util;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class UiUtils {

    public static final String APP_NAME = "WordCraft";

    public static final String LOGOUT_SUCCESS_URL = "/api/dictionary/login";

    public static final String APP_COLOR = "var(--_lumo-button-primary-background-color, var(--lumo-primary-color))";

    public static void showNotification(String message) {
        Notification notification = new Notification(message, 5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    public static void showSuccess(String message) {
        Notification notification =
                Notification.show(message);

        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public static Button getCloseButton() {
        Button close = new Button();
        close.setIcon(new Icon(VaadinIcon.CLOSE));
        close.addThemeVariants(ButtonVariant.LUMO_ERROR);

        return close;
    }
}
