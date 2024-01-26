package com.example.dictionary.ui.users;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.ui.MainLayout;
import com.example.dictionary.ui.util.UiUtils;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;
import static com.example.dictionary.ui.util.UiUtils.getConfiguredSearchField;

@Route(value = "/users", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@PageTitle("Users | " + APP_NAME)
public class UsersView extends VerticalLayout {

    private final UserFacade userFacade;

    private Grid<UserDto> userGrid;

    private TextField searchField;

    public UsersView(UserFacade userFacade) {
        this.userFacade = userFacade;

        setupSearchField();
        setupUserGrid();

        setHorizontalComponentAlignment(Alignment.CENTER, searchField, userGrid);
    }

    private void setupSearchField() {
        searchField = getConfiguredSearchField(getTranslation("search"));
        searchField.addValueChangeListener(event -> resetFilteredData());
        searchField.setWidth("50%");
        add(searchField);
    }

    private void setupUserGrid() {
        userGrid = new Grid<>(UserDto.class, false);
        List<UserDto> users = userFacade.getAllUsers();

        userGrid.setItems(users);
        userGrid.addComponentColumn(UiUtils::getAvatar)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setWidth("5%");
        userGrid.addColumn(UserDto::getFirstName)
                .setHeader(getTranslation("firstname"))
                .setKey("firstName")
                .setSortable(true)
                .setAutoWidth(true);
        userGrid.addColumn(UserDto::getLastName)
                .setHeader(getTranslation("lastname"))
                .setKey("lastName")
                .setSortable(true)
                .setAutoWidth(true);
        userGrid.addColumn(UserDto::getEmail)
                .setHeader(getTranslation("email"))
                .setSortable(true)
                .setAutoWidth(true);
        userGrid.addColumn(UserDto::getRegisteredAt)
                .setHeader(getTranslation("users.registered.date"))
                .setSortable(true)
                .setAutoWidth(true);
        userGrid.addColumn(userDto -> userDto.getKey().toUpperCase())
                .setHeader(getTranslation("users.role"))
                .setSortable(true);

        userGrid.sort(List.of(new GridSortOrder<>(userGrid.getColumnByKey("firstName"),
                SortDirection.ASCENDING)));
        userGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        userGrid.setAllRowsVisible(true);
        userGrid.setWidth("50%");
        add(userGrid);
    }

    private void resetFilteredData() {
        userGrid.setItems(
                userFacade.getAllUsers()
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
}