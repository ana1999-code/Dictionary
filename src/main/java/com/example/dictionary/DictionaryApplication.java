package com.example.dictionary;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@Theme("dictionary-app")
public class DictionaryApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(DictionaryApplication.class, args);
    }

}
