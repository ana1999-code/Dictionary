package com.example.dictionary.application.report.util;

import net.sf.dynamicreports.report.builder.component.CurrentDateBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.PageNumberBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.BorderBuilder;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;
import static java.awt.Color.BLACK;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER;
import static net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT;
import static net.sf.dynamicreports.report.constant.LineStyle.SOLID;

public class ReportUtils {

    public static final Color LIGHT_BLUE = new Color(204, 229, 255);

    public static final Color DARK_BLUE = new Color(1, 1, 43);

    public static final Color BLUE = new Color(0, 95, 219);

    public static final String DD_MM_YYYY = "dd-MM-yyyy";

    public static final String CURRENT_DATE_PATTERN = "EEEEE dd MMMMM";

    public static final String PATH = "src/main/resources/reports/";

    public static final String PDF = ".pdf";

    public static String filePath;

    public static final String WORD_CONTRIBUTIONS = "Word_Contributions_";

    public static final String WORD_STATISTICS = "Word_Statistics_";

    public static final String DD_MMYYYY_HHMMSS = "ddMMyyyyHHmmss";

    public static final int MARGIN = 50;

    public static final int COLUMN_SPACE = 10;

    public static final int PADDING = 30;

    public static final int SUBREPORT_PADDING = 10;

    public static final int SUBREPORT_LEFT_PADDING = 25;

    public static final int SUBREPORT_RIGHT_PADDING = 40;

    public static final int FONT_SIZE = 12;

    public static final float LINE_WIDTH = 0.1f;

    public static HorizontalListBuilder getFooterComponents() {
        return cmp
                .horizontalList(
                        getCurrentDate(),
                        getPageXofY()
                );
    }

    public static HorizontalListBuilder getPageHeader(String currentUser) {
        return cmp.horizontalList(cmp.text(APP_NAME),
                cmp.text("Reported by " + currentUser)
                        .setHorizontalTextAlignment(RIGHT));
    }

    public static StyleBuilder getPageHeaderStyle() {
        return stl.style().setBottomPadding(PADDING);
    }

    public static StyleBuilder getPageFooterStyle() {
        return stl.style().setTopPadding(PADDING);
    }

    public static FileOutputStream getOutputStream(String fileName) throws FileNotFoundException {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MMYYYY_HHMMSS);
        filePath = PATH + fileName + formatter.format(dateTime) + PDF;
        return new FileOutputStream(filePath);
    }

    public static BorderBuilder getBorderBuilder() {
        return stl
                .border(getPenBuilder());
    }

    public static PenBuilder getPenBuilder() {
        return stl
                .pen(LINE_WIDTH, SOLID)
                .setLineColor(BLACK);
    }

    public static CurrentDateBuilder getCurrentDate() {
        return cmp
                .currentDate()
                .setPattern(CURRENT_DATE_PATTERN);
    }

    public static PageNumberBuilder getPageXofY() {
        return cmp
                .pageNumber()
                .setHorizontalTextAlignment(RIGHT);
    }

    public static TextFieldBuilder<String> getTitle(String title) {
        return cmp.text(title)
                .setStyle(stl
                        .style()
                        .setFontSize(FONT_SIZE)
                        .bold()
                        .setForegroundColor(DARK_BLUE))
                .setHorizontalTextAlignment(CENTER)
                .setHeight(SUBREPORT_RIGHT_PADDING);
    }

    public static StyleBuilder getDetailStyle() {
        return stl.style()
                .setBorder(getBorderBuilder());
    }

    public static StyleBuilder getSubreportTitleStyle() {
        return stl.style()
                .bold()
                .setBackgroundColor(LIGHT_BLUE)
                .setLeftPadding(SUBREPORT_LEFT_PADDING)
                .setBottomPadding(SUBREPORT_PADDING)
                .setTopPadding(SUBREPORT_PADDING)
                .setBorder(getBorderBuilder());
    }

    public static StyleBuilder getSubreportColumnStyle() {
        return stl.style().setLeftPadding(60);
    }

    public static TextFieldBuilder<String> getSubreportSummary(TextFieldBuilder<String> summarySubtotal) {
        return summarySubtotal.setStyle(
                stl.style()
                        .setHorizontalTextAlignment(RIGHT)
                        .setRightPadding(SUBREPORT_RIGHT_PADDING)
                        .setBottomPadding(SUBREPORT_PADDING)
                        .setTopPadding(SUBREPORT_PADDING)
                        .setFont(stl.font().italic())
                        .setBorder(getBorderBuilder())
        );
    }
}
