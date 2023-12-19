package com.example.dictionary.application.security.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import static com.example.dictionary.ui.util.UiUtils.LOGOUT_SUCCESS_URL;

@Component
public class SecurityService {

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest
                .getCurrent()
                .getHttpServletRequest(), null, null);
    }
}
