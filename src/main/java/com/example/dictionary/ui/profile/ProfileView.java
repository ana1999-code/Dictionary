package com.example.dictionary.ui.profile;

import com.example.dictionary.application.facade.AchievementFacade;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.ui.MainLayout;
import com.example.dictionary.ui.security.CurrentUserPermissionService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Profile | " + APP_NAME)
public class ProfileView extends VerticalLayout {

    private final UserFacade userFacade;

    private final AchievementFacade achievementFacade;

    private final CurrentUserPermissionService userPermissionService;

    public ProfileView(UserFacade userFacade,
                       AchievementFacade achievementFacade,
                       CurrentUserPermissionService userPermissionService) {
        this.userFacade = userFacade;
        this.achievementFacade = achievementFacade;
        this.userPermissionService = userPermissionService;


        VerticalLayout layout = new VerticalLayout();
        ProfileForm profileForm = new ProfileForm();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();

        ProfileFormBinder profileFormBinder = new ProfileFormBinder(profileForm, userFacade);
        profileFormBinder.addBindingAndValidation();

        UserProgressLayout progressLayout =
                new UserProgressLayout(userPermissionService, userFacade, achievementFacade);
        layout.add(profileForm, progressLayout);
        layout.setHorizontalComponentAlignment(Alignment.CENTER, profileForm, progressLayout);

        add(layout);
    }
}
