package com.example.dictionary.application.security.ui;

import com.example.dictionary.application.security.utils.SecurityUtils;
import com.example.dictionary.ui.login.LoginView;
import com.example.dictionary.ui.registration.RegistrationView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class UiServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()
                && !RegistrationView.class.equals(event.getNavigationTarget())) {
            event.rerouteTo(LoginView.class);
        }
    }
}