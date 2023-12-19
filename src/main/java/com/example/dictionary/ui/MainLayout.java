package com.example.dictionary.ui;

import com.example.dictionary.application.security.ui.SecurityService;
import com.example.dictionary.ui.view.ProfileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
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

        tabs.add(createTab("Profile", ProfileView.class), createTab("Words", ProfileView.class),
                createTab("Users", ProfileView.class), createTab("Logout", new RouterLink()));

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
