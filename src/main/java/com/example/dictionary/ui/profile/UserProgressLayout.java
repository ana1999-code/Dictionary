package com.example.dictionary.ui.profile;

import com.example.dictionary.application.dto.AchievementDto;
import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.dto.UserInfoDto;
import com.example.dictionary.application.facade.AchievementFacade;
import com.example.dictionary.application.facade.UserFacade;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Set;

import static com.example.dictionary.ui.util.UiUtils.APP_COLOR;

@Route("users")
@PermitAll
public class UserProgressLayout extends VerticalLayout {

    public UserProgressLayout(UserFacade userFacade, AchievementFacade achievementFacade) {

        UserDto profile = userFacade.getUserProfile();
        UserInfoDto userInfo = profile.getUserInfo();

        Integer progress = userInfo.getProgress();
        Span progressSpan = new Span(progress.toString());
        progressSpan.getStyle().set("color", APP_COLOR)
                .set("font-weight", "bold");

        Span progressText = new Span("Number of contributions to words: ");
        HorizontalLayout progressLayout = new HorizontalLayout(progressText, progressSpan);

        Span achievementsTitle = new Span("Achievements:");

        VerticalLayout achievementsLayout = new VerticalLayout();
        Icon achievedIcon = new Icon(VaadinIcon.CHECK_CIRCLE_O);
        achievedIcon.setColor(APP_COLOR);

        Set<AchievementDto> userAchievements = userInfo.getAchievements();
        List<AchievementDto> allAchievements = achievementFacade.getAllAchievements();

        for (AchievementDto achievement : allAchievements) {
            Span achievementName = new Span(achievement.getName());
            if (userAchievements.contains(achievement)) {
                HorizontalLayout ach = new HorizontalLayout(achievementName, new Div(achievedIcon));
                achievementsLayout.add(ach);
            } else {
                ProgressBar progressBar = new ProgressBar(0, achievement.getNumberOfWordsRequired(), progress);
                HorizontalLayout achievementWithProgressLayout = new HorizontalLayout(achievementName, progressBar);
                achievementsLayout.add(achievementWithProgressLayout);
            }
        }
        add(progressLayout, achievementsTitle, achievementsLayout);
    }
}
