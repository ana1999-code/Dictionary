package com.example.dictionary.application.security.ui;

import com.example.dictionary.ui.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@Profile("view")
public class ViewSecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Override
    protected void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/icons/**"),
                new AntPathRequestMatcher("/images/**"),
                new AntPathRequestMatcher("/robots.txt"),
                new AntPathRequestMatcher("/manifest.webmanifest"),
                new AntPathRequestMatcher("/sw.js"),
                new AntPathRequestMatcher("/offline.html"),
                new AntPathRequestMatcher("/styles/**"),
                new AntPathRequestMatcher("/h2-console/**")
        );
    }
}
