package com.example.dictionary.ui;

import com.example.dictionary.application.security.ui.SecurityService;
import com.example.dictionary.ui.profile.ProfileView;
import com.example.dictionary.ui.report.ReportView;
import com.example.dictionary.ui.security.CurrentUserPermissionService;
import com.example.dictionary.ui.users.UsersView;
import com.example.dictionary.ui.words.WordsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    private final CurrentUserPermissionService permissionService;

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
                if ("logout".equals(id)) {
                    securityService.logout();
                }
            });
        });

        addToNavbar(title, tabs);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");

        Tab profile = createTab("Profile", ProfileView.class);
        Tab reports = createTab("Reports", ReportView.class);
        Tab words = createTab("Words", WordsView.class);
        Tab users = createTab("Users", UsersView.class);
        Tab logout = createTab("Logout", new RouterLink());

        if (!permissionService.isAdmin()) {
            reports.setEnabled(false);
            users.setEnabled(false);
        }

        tabs.add(profile,
                words,
                users,
                reports,
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
