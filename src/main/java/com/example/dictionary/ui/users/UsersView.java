package com.example.dictionary.ui.users;

import com.example.dictionary.domain.entity.User;
import com.example.dictionary.domain.service.UserService;
import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.dictionary.ui.util.UiUtils.getConfiguredSearchField;

@Route(value = "/users", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class UsersView extends VerticalLayout {

    private final UserService userService;

    private Grid<User> userGrid;

    private TextField searchField;

    public UsersView(UserService userService) {
        this.userService = userService;

        setupSearchField();
        setupUserGrid();

        setHorizontalComponentAlignment(Alignment.CENTER, searchField, userGrid);
    }

    private void setupSearchField() {
        searchField = getConfiguredSearchField();
        searchField.addValueChangeListener(event -> resetFilteredData());
        searchField.setWidth("50%");
        add(searchField);
    }

    private void setupUserGrid() {
        userGrid = new Grid<>(User.class, false);
        List<User> users = userService.getAllUsers();

        userGrid.setItems(users);
        userGrid.addComponentColumn(UsersView::getAvatar)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setWidth("5%");
        userGrid.addColumn(User::getFirstName)
                .setHeader("First Name")
                .setKey("firstName")
                .setSortable(true)
                .setAutoWidth(true);
        userGrid.addColumn(User::getLastName)
                .setHeader("Last Name")
                .setKey("lastName")
                .setSortable(true)
                .setAutoWidth(true);
        userGrid.addColumn(User::getEmail)
                .setHeader("Email")
                .setSortable(true)
                .setAutoWidth(true);
        userGrid.addColumn(User::getRegisteredAt)
                .setHeader("Register Date")
                .setSortable(true)
                .setAutoWidth(true);
        userGrid.addColumn(User::getRole)
                .setHeader("Role");

        userGrid.sort(List.of(new GridSortOrder<>(userGrid.getColumnByKey("firstName"),
                SortDirection.ASCENDING)));
        userGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        userGrid.setAllRowsVisible(true);
        userGrid.setWidth("50%");
        add(userGrid);
    }


    private void resetFilteredData() {
        userGrid.setItems(
                userService.getAllUsers()
                        .stream()
                        .filter(user ->
                                user
                                        .getFirstName()
                                        .toLowerCase()
                                        .startsWith(searchField.getValue().toLowerCase()) ||
                                        user
                                                .getLastName()
                                                .toLowerCase()
                                                .startsWith(searchField.getValue().toLowerCase()) ||
                                        user
                                                .getEmail()
                                                .toLowerCase()
                                                .startsWith(searchField.getValue().toLowerCase()))
                        .collect(Collectors.toSet())
        );
    }

    private static Avatar getAvatar(User user) {
        String name = user.getFirstName() + " " + user.getLastName();
        Avatar avatar = new Avatar(name);
        avatar.setColorIndex(name.length());
        return avatar;
    }
}