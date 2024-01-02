package com.example.dictionary.ui.report;

import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.report.WordsContributionReport;
import com.example.dictionary.application.report.data.WordDetail;
import com.example.dictionary.domain.repository.WordRepository;
import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.FileNotFoundException;
import java.util.List;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;
import static com.example.dictionary.ui.util.UiUtils.showNotification;

@Route(value = "reports", layout = MainLayout.class)
@PageTitle("Reports | " + APP_NAME)
@RolesAllowed("ADMIN")
public class ReportView extends VerticalLayout {

    private Button generateReport;

    private WordsContributionReport wordsContributionReport;

    private final WordRepository wordRepository;

    private final UserFacade userFacade;

    public ReportView(WordRepository wordRepository, UserFacade userFacade) {
        this.wordRepository = wordRepository;
        this.userFacade = userFacade;

        List<WordDetail> allWords = wordRepository.getWordsDetails();
        generateReport = new Button("Generate Report");
        wordsContributionReport = new WordsContributionReport(allWords, this.userFacade);
        generateReport.addClickListener(event -> {
            try {
                wordsContributionReport.generate();
            } catch (FileNotFoundException | DRException exception) {
                showNotification(exception.getMessage());
            }
        });
        add(generateReport);
    }
}
