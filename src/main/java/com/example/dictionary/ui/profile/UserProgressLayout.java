package com.example.dictionary.ui.profile;

import com.example.dictionary.application.dto.AchievementDto;
import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.dto.UserInfoDto;
import com.example.dictionary.application.facade.AchievementFacade;
import com.example.dictionary.application.facade.UserFacade;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
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

    private Integer progress;

    private Set<AchievementDto> userAchievements;

    public UserProgressLayout(UserFacade userFacade, AchievementFacade achievementFacade) {
        UserDto profile = userFacade.getUserProfile();
        UserInfoDto userInfo = profile.getUserInfo();

        progress = userInfo.getProgress();
        Span progressLabel = new Span(progress.toString());
        progressLabel.getStyle().set("color", APP_COLOR)
                .set("font-weight", "bold");

        Span progressDescription = new Span("Number of contributions to words: ");
        HorizontalLayout progressLayout = new HorizontalLayout(progressDescription, progressLabel);

        Span achievementsLabel = new Span("Achievements:");

        userAchievements = userInfo.getAchievements();
        List<AchievementDto> allAchievements = achievementFacade.getAllAchievements();

        Grid<AchievementDto> achievementGrid = getAchievementsGrid(allAchievements);
        setWidth("43%");

        add(progressLayout, achievementsLabel, achievementGrid);
    }

    private Grid<AchievementDto> getAchievementsGrid(List<AchievementDto> allAchievements) {
        Grid<AchievementDto> achievementGrid = new Grid<>();
        achievementGrid.setItems(allAchievements);
        achievementGrid.
                addColumn(AchievementDto::getName)
                .setWidth("30%")
                .setTooltipGenerator(achievementDto ->
                        String.valueOf(achievementDto.getNumberOfWordsRequired()));
        achievementGrid.addComponentColumn(this::createProgressBar).setWidth("70%");

        achievementGrid.getStyle().set("border", "none");
        achievementGrid.setWidth("110%");
        return achievementGrid;
    }

    private <V extends Component> Component createProgressBar(AchievementDto achievementDto) {
        if (userAchievements.contains(achievementDto)) {
            Icon icon = new Icon(VaadinIcon.CHECK_CIRCLE_O);
            icon.setColor(APP_COLOR);
            return icon;
        }
        return new ProgressBar(0, achievementDto.getNumberOfWordsRequired(), progress);
    }
}
