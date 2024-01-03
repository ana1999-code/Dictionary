package com.example.dictionary.ui.report;

import com.example.dictionary.application.exception.ValueRequiredException;
import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.report.ReportGenerator;
import com.example.dictionary.application.report.WordsContributionReportGenerator;
import com.example.dictionary.application.report.WordsStatisticReportGenerator;
import com.example.dictionary.application.report.data.WordDetail;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.repository.WordRepository;
import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.RolesAllowed;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.FileNotFoundException;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;
import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;

@Route(value = "reports", layout = MainLayout.class)
@PageTitle("Reports | " + APP_NAME)
@RolesAllowed("ADMIN")
public class ReportView extends VerticalLayout {

    public static final String WORDS_CONTRIBUTIONS_REPORT = "Words Contributions Report";

    public static final String WORDS_STATISTIC_REPORT = "Words Statistic Report";

    private Select<String> selectReportType;

    private Button generateReport;

    private final WordRepository wordRepository;

    private final UserFacade userFacade;

    private List<WordDetail> wordsDetails;

    private List<Word> words;

    private Div reportDescription = new Div();

    private ComboBox<Integer> yearComboBox = new ComboBox<>("Year");

    private ComboBox<Month> monthComboBox = new ComboBox<>("Month");

    private Registration registration;

    public ReportView(WordRepository wordRepository, UserFacade userFacade) {
        this.wordRepository = wordRepository;
        this.userFacade = userFacade;
        wordsDetails = wordRepository.getWordsDetails();
        words = wordRepository.findAll();

        generateReport = new Button("Generate Report");
        reportDescription.setMaxWidth("40%");
        reportDescription.getStyle().set("text-align", "center");

        setupReportTypeSelection();

        HorizontalLayout reportSelectionLayout = new HorizontalLayout(selectReportType, generateReport);
        reportSelectionLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        setHorizontalComponentAlignment(Alignment.CENTER,
                reportSelectionLayout, reportDescription, yearComboBox, monthComboBox
        );
        add(reportSelectionLayout);
    }

    private void setupReportTypeSelection() {
        selectReportType = new Select<>();
        selectReportType.setLabel("Select Report Type");
        selectReportType.setItems(WORDS_CONTRIBUTIONS_REPORT, WORDS_STATISTIC_REPORT);
        selectReportType.addValueChangeListener(event -> {
            String reportTypeValue = event.getValue();
            cleanupReportConfigurations();
            if (reportTypeValue.equalsIgnoreCase(WORDS_CONTRIBUTIONS_REPORT)) {
                setupWordsContributionsReport();
            } else if (reportTypeValue.equalsIgnoreCase(WORDS_STATISTIC_REPORT)) {
                setupWordsStatisticReport();
            }
        });
    }

    private void cleanupReportConfigurations() {
        if (registration != null) {
            registration.remove();
        }
        reportDescription.removeAll();
        remove(reportDescription, yearComboBox, monthComboBox);
    }

    private void setupWordsContributionsReport() {
        reportDescription.add("This report lists users and the words they contributed, " +
                "offering a quick overview of individual contributions to the database.");
        WordsContributionReportGenerator reportGenerator =
                new WordsContributionReportGenerator(wordsDetails, userFacade);
        generateReport(reportGenerator);
        add(reportDescription);
    }

    private void setupWordsStatisticReport() {
        WordsStatisticReportGenerator reportGenerator =
                new WordsStatisticReportGenerator(words, userFacade);
        reportDescription.add("To generate this report, select a specific month and year. " +
                "The resulting chart provides a quick overview of daily word additions during the chosen period, " +
                "aiding in the analysis of database growth patterns.");

        monthComboBox.setEnabled(false);
        setupYearComboBox(reportGenerator);

        generateReport(reportGenerator);
        add(reportDescription, yearComboBox, monthComboBox);
    }

    private void setupYearComboBox(WordsStatisticReportGenerator reportGenerator) {
        Set<Integer> years = wordsDetails.stream()
                .map(wordDetail -> wordDetail.getAddedAt().getYear())
                .collect(Collectors.toSet());

        yearComboBox.setItems(years);
        yearComboBox.setRequired(true);

        yearComboBox.addValueChangeListener(event -> {
            Integer year = event.getValue();
            reportGenerator.setYear(year);
            setupMonthComboBox(reportGenerator, year);
        });
    }

    private void setupMonthComboBox(WordsStatisticReportGenerator reportGenerator, Integer year) {
        monthComboBox.setEnabled(true);
        Set<Month> months = wordsDetails.stream()
                .filter(wordDetail -> wordDetail.getAddedAt().getYear() == year)
                .map(wordDetail -> wordDetail.getAddedAt().getMonth())
                .collect(Collectors.toSet());

        monthComboBox.setItems(months);
        monthComboBox.setRequired(true);

        monthComboBox.addValueChangeListener(event -> {
            Month month = event.getValue();
            reportGenerator.setMonth(month);
        });
    }

    private void generateReport(ReportGenerator reportGenerator) {
        registration = generateReport.addClickListener(event -> {
            try {
                reportGenerator.generate();
                showSuccess("Report Successfully Generated");
            } catch (FileNotFoundException | DRException | ValueRequiredException exception) {
                showNotification(exception.getMessage());
            }
        });
    }
}
