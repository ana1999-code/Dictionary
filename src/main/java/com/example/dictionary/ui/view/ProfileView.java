package com.example.dictionary.ui.view;

import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Profile | Dictionary")
public class ProfileView extends VerticalLayout {

    public ProfileView() {
        add(new H1("Profile"));
    }
}
