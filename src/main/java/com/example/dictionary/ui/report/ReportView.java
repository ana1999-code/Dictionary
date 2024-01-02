package com.example.dictionary.ui.report;

import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.report.WordsReport;
import com.example.dictionary.application.report.data.WordDetail;
import com.example.dictionary.domain.repository.WordRepository;
import com.example.dictionary.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;

@Route(value = "reports", layout = MainLayout.class)
@PageTitle("Reports | " + APP_NAME)
@PermitAll
public class ReportView extends VerticalLayout {

    private Button generateReport;

    private WordsReport wordsReport;

    private final WordRepository wordRepository;

    private final UserFacade userFacade;

    public ReportView(WordRepository wordRepository, UserFacade userFacade) {
        this.wordRepository = wordRepository;
        this.userFacade = userFacade;

        List<WordDetail> allWords = wordRepository.getWordsDetails();
        generateReport = new Button("Generate Report");
        wordsReport = new WordsReport(allWords, this.userFacade);
        generateReport.addClickListener(event -> wordsReport.generate());
        add(generateReport);
    }
}
