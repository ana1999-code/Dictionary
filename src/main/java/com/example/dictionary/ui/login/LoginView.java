package com.example.dictionary.ui.login;

import com.example.dictionary.application.security.utils.SecurityUtils;
import com.example.dictionary.ui.view.ProfileView;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | Dictionary")
@AnonymousAllowed
public class LoginView extends LoginOverlay implements AfterNavigationObserver, BeforeEnterObserver {

    public LoginView() {
        LoginI18n loginI18n = LoginI18n.createDefault();
        loginI18n.setHeader(new LoginI18n.Header());
        loginI18n.getHeader().setTitle("Dictionary");
        loginI18n.setAdditionalInformation(null);
        loginI18n.setForm(new LoginI18n.Form());
        loginI18n.getForm().setSubmit("Sign in");
        loginI18n.getForm().setUsername("Email");
        loginI18n.getForm().setPassword("Password");
        setI18n(loginI18n);
        setForgotPasswordButtonVisible(false);
        setAction("login");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (SecurityUtils.isUserLoggedIn()){
            event.forwardTo(ProfileView.class);
        } else {
            setOpened(true);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        setError(
                event.getLocation().getQueryParameters().getParameters().containsKey(
                        "error"
                )
        );
    }
}
