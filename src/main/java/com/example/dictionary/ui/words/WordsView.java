package com.example.dictionary.ui.words;

import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.facade.CategoryFacade;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.MainLayout;
import com.example.dictionary.ui.security.CurrentUserPermissionService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;
import static com.example.dictionary.ui.util.UiUtils.CSV;
import static com.example.dictionary.ui.util.UiUtils.DD_MM_YYYY;
import static com.example.dictionary.ui.util.UiUtils.FILE_LOCATION;
import static com.example.dictionary.ui.util.UiUtils.PROCESSED;
import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;
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
        tabLayout.setWidth("75%");
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
        uploadFile.setAcceptedFileTypes(CSV);
        uploadFile.setUploadButton(new Button("Upload File", new Icon(VaadinIcon.UPLOAD)));
        uploadFile.setReceiver(memoryBuffer);
        uploadFile.addFileRejectedListener(event -> showNotification(event.getErrorMessage()));

        uploadFile.addSucceededListener(event -> {
            try {
                String csvFilePath = getCsvFilePath(memoryBuffer);

                wordFacade.uploadFile(csvFilePath, memoryBuffer.getFileName(), FILE_LOCATION);
                uploadFile.clearFileList();
                wordDtoGrid.setItems(wordFacade.getAllWords());
            } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                     JobParametersInvalidException | JobRestartException | IOException | CsvException exception) {
                showNotification(exception.getMessage());
            }
        });
    }

    private static String getCsvFilePath(MemoryBuffer memoryBuffer) throws IOException, CsvException {
        InputStream inputStream = memoryBuffer.getInputStream();
        String fileName = memoryBuffer.getFileName();
        CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
        String csvFilePath = FILE_LOCATION + PROCESSED + fileName;

        try (CSVReader reader =
                     new CSVReaderBuilder(new InputStreamReader(inputStream)).withCSVParser(parser).build();
             CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            List<String[]> data = reader.readAll();
            writer.writeAll(data);
        } catch (IOException exception) {
            showNotification(exception.getMessage());
        }

        return csvFilePath;
    }

    private void setupAddButton() {
        addWord = new Button("Add Word");
        addWord.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addWord.setIcon(new Icon(VaadinIcon.PLUS));

        wordForm = new WordForm(wordFacade, categoryFacade);
        dialogForm = new WordDialog(wordForm);
        dialog = dialogForm.getDialog();

        addWord.addClickListener(event -> dialog.open());
        Button saveButton = dialogForm.getSaveButton();
        saveButton.addClickListener(event -> {
            try {
                wordForm.saveWord();
                wordDtoGrid.setItems(wordFacade.getAllWords());
                showSuccess("Word [%s] successfully added".formatted(wordForm.getName()));
                refreshWordForm();
            } catch (ValidationException ignored) {
            } catch (RuntimeException exception) {
                showNotification(exception.getMessage());
            }
        });

        Button cancelButton = dialogForm.getCancelButton();
        cancelButton.getStyle().set("margin-right", "auto");
        cancelButton.addClickListener(event -> {
            refreshWordForm();
        });

        Button resetButton = dialogForm.getResetButton();
        resetButton.addClickListener(event -> wordForm.reset());

        add(dialog);
    }

    private void setupSearchField() {
        searchField = new TextField();
        searchField.setPlaceholder("Search...");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(
                event -> wordDtoGrid.setItems(
                        wordFacade.getAllWords()
                                .stream()
                                .filter(word -> word
                                        .getName()
                                        .toLowerCase()
                                        .startsWith(searchField.getValue().toLowerCase()))
                                .collect(Collectors.toSet())
                ));
        searchField.setSuffixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setWidthFull();
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
        List<WordDto> words = wordFacade.getAllWords();

        wordDtoGrid.setItems(words);
        wordDtoGrid.addColumn(WordDto::getName)
                .setHeader("Word");
        wordDtoGrid.addColumn(wordDto -> wordDto.getCategory().getName())
                .setHeader("Category");
        wordDtoGrid.addColumn(wordDto -> wordDto.getDefinitions().size())
                .setHeader("Nr. Definitions");
        wordDtoGrid.addColumn(wordDto -> wordDto.getSynonyms().size())
                .setHeader("Nr. Synonyms");
        wordDtoGrid.addColumn(wordDto -> wordDto.getAntonyms().size())
                .setHeader("Nr. Antonyms");
        wordDtoGrid.addColumn(wordDto -> wordDto.getExamples().size())
                .setHeader("Nr. Examples");
        wordDtoGrid.addColumn(
                        new LocalDateTimeRenderer<>(WordDto::getAddedAt, DD_MM_YYYY)
                )
                .setKey("addedAt")
                .setSortProperty("addedAt")
                .setComparator(Comparator.comparing(WordDto::getAddedAt))
                .setHeader("Added At");
        wordDtoGrid.getColumns()
                .forEach(col -> col
                        .setSortable(true)
                        .setSortProperty("")
                        .setTextAlign(CENTER)
                        .setAutoWidth(false));
        wordDtoGrid.addComponentColumn(this::getHeartButton)
                .setTextAlign(CENTER)
                .setWidth("5%");

        wordDtoGrid.sort(
                List.of(new GridSortOrder<>(wordDtoGrid.getColumnByKey("addedAt"),
                        SortDirection.DESCENDING))
        );

        wordDtoGrid.setAllRowsVisible(true);
        wordDtoGrid.setPageSize(10);
        wordDtoGrid.setWidth("75%");
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
