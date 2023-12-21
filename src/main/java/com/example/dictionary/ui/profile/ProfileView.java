package com.example.dictionary.ui.profile;

import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Profile | Dictionary")
public class ProfileView extends VerticalLayout {

    private final UserFacade userFacade;

    public ProfileView(UserFacade userFacade) {
        this.userFacade = userFacade;
        ProfileForm profileForm = new ProfileForm();
        setHorizontalComponentAlignment(Alignment.CENTER, profileForm);
        add(profileForm);
        setSizeFull();

        ProfileFormBinder profileFormBinder = new ProfileFormBinder(profileForm, userFacade);
        profileFormBinder.addBindingAndValidation();
    }
}
