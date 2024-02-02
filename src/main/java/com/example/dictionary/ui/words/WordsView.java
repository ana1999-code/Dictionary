package com.example.dictionary.ui.words;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.CategoryFacade;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.MainLayout;
import com.example.dictionary.ui.security.CurrentUserPermissionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import jakarta.annotation.security.PermitAll;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.data.domain.Sort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;
import static com.example.dictionary.ui.util.UiUtils.CSV;
import static com.example.dictionary.ui.util.UiUtils.DD_MM_YYYY;
import static com.example.dictionary.ui.util.UiUtils.FILE_LOCATION;
import static com.example.dictionary.ui.util.UiUtils.JSON;
import static com.example.dictionary.ui.util.UiUtils.PROCESSED;
import static com.example.dictionary.ui.util.UiUtils.getConfiguredSearchField;
import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY;
import static com.vaadin.flow.component.grid.ColumnTextAlign.CENTER;

@Route(value = "words", layout = MainLayout.class)
@PermitAll
@PageTitle("Words | " + APP_NAME)
public class WordsView extends VerticalLayout {

    private final CurrentUserPermissionService permissionService;

    private WordForm wordForm;

    private WordDialog dialogForm;

    private Dialog dialog;

    private Grid<WordDto> wordDtoGrid;

    private final WordFacade wordFacade;

    private final UserFacade userFacade;

    private final CategoryFacade categoryFacade;

    private Set<String> userFavoriteWords;

    private HorizontalLayout tabLayout;

    private TextField searchField;

    private Upload uploadFile;

    private Button addWord;

    public WordsView(CurrentUserPermissionService permissionService,
                     WordFacade wordFacade,
                     UserFacade userFacade,
                     CategoryFacade categoryFacade) {
        this.permissionService = permissionService;
        this.wordFacade = wordFacade;
        this.userFacade = userFacade;
        this.categoryFacade = categoryFacade;

        setupWordsGrid();
        setupSearchField();
        setupUserFavoriteWords();

        tabLayout =
                new HorizontalLayout(searchField);
        setupButtonsForWritePermission();
        tabLayout.setWidth("60%");
        tabLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        setHorizontalComponentAlignment(Alignment.CENTER, tabLayout, this.wordDtoGrid);
        setSizeFull();
        add(tabLayout, this.wordDtoGrid);
    }

    private void setupButtonsForWritePermission() {
        if (permissionService.hasWordWritePermission()) {
            setupUploadButton();
            setupAddButton();
            tabLayout.add(uploadFile, addWord);
        }
    }

    private void setupUploadButton() {
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        uploadFile = new Upload();
        uploadFile.setDropAllowed(false);
        uploadFile.setAcceptedFileTypes(JSON, CSV);
        uploadFile.setUploadButton(new Button(getTranslation("words.upload"), new Icon(VaadinIcon.UPLOAD)));
        uploadFile.setReceiver(memoryBuffer);
        uploadFile.addFileRejectedListener(event -> showNotification(getTranslation("upload.file.type.error")));

        uploadFile.addSucceededListener(event -> {
            try {
                Files.createDirectories(Paths.get(FILE_LOCATION));
                String csvFilePath = getFilePath(memoryBuffer);

                wordFacade.uploadFile(csvFilePath, memoryBuffer.getFileName(), FILE_LOCATION);
                uploadFile.clearFileList();
                wordDtoGrid.setItems(wordFacade.getAllWords());
            } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                     JobParametersInvalidException | JobRestartException | IOException exception) {
                showNotification(exception.getMessage());
            }
        });
    }

    private static String getFilePath(MemoryBuffer memoryBuffer) throws IOException {
        InputStream inputStream = memoryBuffer.getInputStream();
        String fileName = memoryBuffer.getFileName();

        String processedFilePath = FILE_LOCATION + PROCESSED + fileName;

        try (OutputStream outputStream = new FileOutputStream(processedFilePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException exception) {
            showNotification(exception.getMessage());
        }

        return processedFilePath;
    }

    private void setupAddButton() {
        addWord = new Button(getTranslation("add") + " " + getTranslation("word.name"));
        addWord.addThemeVariants(LUMO_PRIMARY);
        addWord.setIcon(new Icon(VaadinIcon.PLUS));

        wordForm = new WordForm(wordFacade, categoryFacade);
        dialogForm = new WordDialog(wordForm, getTranslation("words.new"));
        dialog = dialogForm.getDialog();

        addWord.addClickListener(event -> dialog.open());

        setupSaveButton();
        setupCancelButton();
        setupResetButton();

        add(dialog);
    }

    private void setupSaveButton() {
        Button saveButton = dialogForm.getSecondRightButton();
        saveButton.setText(getTranslation("save"));
        saveButton.addThemeVariants(LUMO_PRIMARY);
        saveButton.addClickListener(event -> {
            try {
                wordForm.saveWord();
                wordDtoGrid.setItems(wordFacade.getAllWords());
                showSuccess(getTranslation("word.message.success", wordForm.getName()));
                refreshWordForm();
            } catch (ValidationException ignored) {
            } catch (RuntimeException exception) {
                showNotification(exception.getMessage());
            }
        });
    }

    private void setupCancelButton() {
        Button cancelButton = dialogForm.getLeftButton();
        cancelButton.setText(getTranslation("cancel"));
        cancelButton.addThemeVariants(LUMO_ERROR);
        cancelButton.addClickListener(event -> {
            refreshWordForm();
        });
    }

    private void setupResetButton() {
        Button resetButton = dialogForm.getFirstRightButton();
        resetButton.setText(getTranslation("reset"));
        resetButton.addThemeVariants(LUMO_TERTIARY);
        resetButton.addClickListener(event -> wordForm.reset());
    }

    private void setupSearchField() {
        searchField = getConfiguredSearchField(getTranslation("search"));
        searchField.addValueChangeListener(event -> resetFilteredData());
    }

    private void resetFilteredData() {
        wordDtoGrid.setItems(
                wordFacade.getAllWords()
                        .stream()
                        .filter(word -> word
                                .getName()
                                .toLowerCase()
                                .startsWith(searchField.getValue().toLowerCase()))
                        .collect(Collectors.toSet())
        );
    }

    private void setupUserFavoriteWords() {
        userFavoriteWords = userFacade.getUserProfile()
                .getUserInfo()
                .getFavorites()
                .stream()
                .map(WordDto::getName)
                .collect(Collectors.toSet());
    }

    private void setupWordsGrid() {
        wordDtoGrid = new Grid<>(WordDto.class, false);

        setWordColumns();
        addItemClickListener();
        addWordSorting();

        setupWordGridStyle();
    }

    private void setWordColumns() {
        wordDtoGrid.setItems(query -> {
            Sort sort = toSpringDataSort(query.getSortOrders());
            return wordFacade.getAllWords(query.getPage(), query.getPageSize(), sort).stream();
        });
        wordDtoGrid.addColumn(WordDto::getName)
                .setHeader(getTranslation("word.name"))
                .setFooter(getTranslation("words.total", wordFacade.getAllWords().size()))
                .setKey("name")
                .setSortProperty("name");
        wordDtoGrid.addColumn(wordDto -> wordDto.getCategory().getName())
                .setHeader(getTranslation("word.category"))
                .setKey("category")
                .setSortProperty("category");
        wordDtoGrid.addColumn(wordDto -> wordDto.getDefinitions().size())
                .setHeader(getTranslation("words.nr") + " " + getTranslation("word.definitions"));
        wordDtoGrid.addColumn(wordDto -> wordDto.getSynonyms().size())
                .setHeader(getTranslation("words.nr") + " " + getTranslation("word.synonyms"));
        wordDtoGrid.addColumn(wordDto -> wordDto.getAntonyms().size())
                .setHeader(getTranslation("words.nr") + " " + getTranslation("word.antonyms"));
        wordDtoGrid.addColumn(wordDto -> wordDto.getExamples().size())
                .setHeader(getTranslation("words.nr") + " " + getTranslation("word.examples"));
        wordDtoGrid.addColumn(
                        new LocalDateTimeRenderer<>(WordDto::getAddedAt, DD_MM_YYYY)
                )
                .setKey("addedAt")
                .setSortProperty("addedAt")
                .setComparator(Comparator.comparing(WordDto::getAddedAt))
                .setHeader(getTranslation("words.added"));
        wordDtoGrid.getColumns()
                .forEach(col -> col
                        .setTextAlign(CENTER)
                        .setAutoWidth(false));
        wordDtoGrid.addComponentColumn(this::getHeartButton)
                .setTextAlign(CENTER)
                .setWidth("5%");
    }

    public static Sort toSpringDataSort(List<QuerySortOrder> vaadinSortOrders) {
        return Sort.by(
                vaadinSortOrders.stream()
                        .map(sortOrder ->
                                sortOrder.getDirection() == SortDirection.ASCENDING ?
                                        Sort.Order.asc(sortOrder.getSorted()) :
                                        Sort.Order.desc(sortOrder.getSorted())
                        )
                        .collect(Collectors.toList())
        );
    }

    private void addItemClickListener() {
        wordDtoGrid.addItemDoubleClickListener(event -> {
            String wordName = event.getItem().getName();
            wordDtoGrid.getUI().ifPresent(ui -> ui.navigate(WordView.class,
                    new RouteParameters("wordName", wordName)));
        });
    }

    private void addWordSorting() {
        wordDtoGrid.sort(
                List.of(new GridSortOrder<>(wordDtoGrid.getColumnByKey("addedAt"),
                        SortDirection.DESCENDING))
        );
    }

    private void setupWordGridStyle() {
        wordDtoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        wordDtoGrid.setPageSize(10);
        wordDtoGrid.setWidth("60%");
    }

    private Button getHeartButton(WordDto wordDto) {
        Button favButton = new Button();
        AtomicBoolean isClicked = new AtomicBoolean(false);

        isClicked.set(userFavoriteWords.contains(wordDto.getName()));

        favButton.setIcon(updateHeartIcon(isClicked.get()));
        favButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        favButton.addClickListener(event -> {
            isClicked.set(!isClicked.get());
            updateFavoriteWords(wordDto.getName(), isClicked.get());
            favButton.setIcon(updateHeartIcon(isClicked.get()));
        });

        return favButton;
    }

    private void updateFavoriteWords(String wordName, boolean isClicked) {
        if (isClicked) {
            userFacade.addWordToFavorities(wordName);
        } else {
            userFacade.removeWordFromFavorites(wordName);
        }
    }

    private Icon updateHeartIcon(boolean isClicked) {
        if (isClicked) {
            return new Icon(VaadinIcon.HEART);
        } else {
            return new Icon(VaadinIcon.HEART_O);
        }
    }

    private void refreshWordForm() {
        dialog.close();
        wordForm.reset();
    }
}
