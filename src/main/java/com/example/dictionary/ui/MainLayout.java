package com.example.dictionary.ui;

import com.example.dictionary.application.security.ui.SecurityService;
import com.example.dictionary.ui.i18n.SimpleI18NProvider;
import com.example.dictionary.ui.profile.ProfileView;
import com.example.dictionary.ui.quiz.QuizView;
import com.example.dictionary.ui.report.ReportView;
import com.example.dictionary.ui.security.CurrentUserPermissionService;
import com.example.dictionary.ui.users.UsersView;
import com.example.dictionary.ui.words.WordsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;

import java.util.Locale;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    private final CurrentUserPermissionService permissionService;

    private final Select<Locale> languageSelect;

    private final SimpleI18NProvider i18NProvider = SimpleI18NProvider.getSimpleI18NProvider();


    public MainLayout(SecurityService securityService,
                      CurrentUserPermissionService permissionService) {
        this.securityService = securityService;
        this.permissionService = permissionService;
        H1 title = new H1(APP_NAME);
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)").set("margin", "0")
                .set("position", "absolute");

        Tabs tabs = getTabs();
        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab() == null) {
                return;
            }

            event.getSelectedTab().getId().ifPresent(id -> {
                if (getTranslation("tab.logout.name").equalsIgnoreCase(id)) {
                    securityService.logout();
                }
            });
        });

        languageSelect = new Select<>();
        languageSelect.setItems(i18NProvider.getProvidedLocales());
        languageSelect.setRenderer(languageRenderer);
        languageSelect.setValue(UI.getCurrent().getLocale());
        languageSelect.addValueChangeListener(event -> saveLocalePreference(event.getValue()));

        languageSelect.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("right", "var(--lumo-space-l)")
                .set("margin", "0")
                .set("position", "absolute")
                .set("width", "9em");

        addToNavbar(title, tabs, languageSelect);
    }

    private ComponentRenderer<HorizontalLayout, Locale> languageRenderer = new ComponentRenderer<>(item -> {
        HorizontalLayout hLayout = new HorizontalLayout();
        Image languageFlag = new Image("img/languageflags/" + item.getLanguage() + ".jpg", "flag for " + item.getLanguage());
        languageFlag.setHeight("19px");
        hLayout.add(languageFlag);
        hLayout.add(new Span(getTranslation("language." + item.getLanguage())));
        hLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return hLayout;
    });

    private void saveLocalePreference(Locale locale) {
        Locale.setDefault(locale);
        getUI().get().setLocale(locale);
        VaadinService.getCurrentResponse()
                .addCookie(new Cookie("locale", locale.toLanguageTag()));
        UI.getCurrent().getPage().reload();
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");

        Tab profile = createTab(getTranslation("tab.profile.name"), ProfileView.class);
        Tab reports = createTab(getTranslation("tab.reports.name"), ReportView.class);
        Tab words = createTab(getTranslation("tab.words.name"), WordsView.class);
        Tab users = createTab(getTranslation("tab.users.name"), UsersView.class);
        Tab logout = createTab(getTranslation("tab.logout.name"), new RouterLink());
        Tab quizzes = createTab(getTranslation("tab.quiz.name"), QuizView.class);

        if (!permissionService.isAdmin()) {
            reports.setEnabled(false);
            users.setEnabled(false);
        }

        tabs.add(profile,
                words,
                users,
                reports,
                quizzes,
                logout);

        return tabs;
    }

    private Tab createTab(String viewName, Class<? extends Component> viewClass) {
        RouterLink link = new RouterLink();
        link.setRoute(viewClass);

        return createTab(viewName, link);
    }

    private Tab createTab(String viewName, RouterLink link) {
        link.add(viewName);
        link.setTabIndex(-1);

        Tab tab = new Tab(link);
        tab.setId(viewName.toLowerCase());
        return tab;
    }
}
