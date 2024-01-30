package com.example.dictionary.application.report;

import com.example.dictionary.application.exception.ValueRequiredException;
import com.example.dictionary.application.i18n.LocaleConfig;
import com.example.dictionary.domain.entity.Word;
import net.sf.dynamicreports.report.builder.chart.AxisFormatBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.CategoryChartSerieBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.dictionary.application.report.util.ReportUtils.BLUE;
import static com.example.dictionary.application.report.util.ReportUtils.MARGIN;
import static com.example.dictionary.application.report.util.ReportUtils.filePath;
import static com.example.dictionary.application.report.util.ReportUtils.getFooterComponents;
import static com.example.dictionary.application.report.util.ReportUtils.getOutputStream;
import static com.example.dictionary.application.report.util.ReportUtils.getPageFooterStyle;
import static com.example.dictionary.application.report.util.ReportUtils.getPageHeader;
import static com.example.dictionary.application.report.util.ReportUtils.getPageHeaderStyle;
import static com.example.dictionary.application.report.util.ReportUtils.getTitle;
import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.chart.Charts.serie;

public class WordsStatisticReportGenerator implements ReportGenerator {

    private List<Word> words;

    private final String currentUser;

    private Integer year;

    private Month month;

    private int totalWords;

    private final MessageSource messageSource;

    public WordsStatisticReportGenerator(List<Word> words, String currentUser) {
        LocaleConfig localeConfig = new LocaleConfig();
        this.messageSource = localeConfig.messageSource();
        this.words = words;
        this.currentUser = currentUser;
    }

    public void generate() throws IOException, DRException {
        totalWords = 0;
        report()
                .setLocale(Locale.getDefault())
                .title(getTitle(messageSource.getMessage("report.word.statistics.title",
                        new Object[]{getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), year.toString()},
                        Locale.getDefault())))
                .setDataSource(getDataSource())
                .setPageMargin(margin(MARGIN))
                .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                .summary(getBarChartBuilder())
                .pageFooter(getFooterComponents())
                .pageHeader(getPageHeader(currentUser, messageSource))
                .setPageHeaderStyle(getPageHeaderStyle())
                .setPageFooterStyle(getPageFooterStyle())
                .toPdf(getOutputStream(messageSource.getMessage(
                        "report.word.statistics.file.name",
                        null, Locale.getDefault())));
    }

    private DRDataSource getDataSource() {
        validateYearAndMonth();
        Map<Integer, Long> collect = words.stream()
                .filter(wordDetail -> wordDetail.getAddedAt().getYear() == year &&
                        wordDetail.getAddedAt().getMonth().equals(month))
                .collect(Collectors.groupingBy(wordDetail ->
                        wordDetail.getAddedAt().getDayOfMonth(), Collectors.counting()));

        DRDataSource dataSource = new DRDataSource("day", "nrOfWords");

        int monthLength = month.maxLength();
        for (int i = 1; i <= monthLength; i++) {
            dataSource.add(String.valueOf(i), 0);
        }

        for (Integer day : collect.keySet()) {
            dataSource.add(day.toString(), collect.get(day));
            totalWords += collect.get(day);
        }
        return dataSource;
    }

    private void validateYearAndMonth() {
        if (year == null) {
            throw new ValueRequiredException("Select year");
        }

        if (month == null) {
            throw new ValueRequiredException("Select month");
        }
    }

    private BarChartBuilder getBarChartBuilder() {
        return cht.barChart()
                .setTitle(messageSource.getMessage("report.word.statistics.chart.title",
                        null, Locale.getDefault()))
                .seriesColors(BLUE)
                .setCategory(getCategory())
                .series(getSeries())
                .setCategoryAxisFormat(getCategoryAxisFormat())
                .setValueAxisFormat(getValueAxisFormat());
    }

    private AxisFormatBuilder getValueAxisFormat() {
        return cht.axisFormat().setLabel(messageSource.getMessage("report.word.statistics.ox.title",
                null, Locale.getDefault()));
    }

    private AxisFormatBuilder getCategoryAxisFormat() {
        return cht.axisFormat().setLabel(messageSource.getMessage("report.word.statistics.oy.title",
                null, Locale.getDefault()));
    }

    private CategoryChartSerieBuilder getSeries() {
        return serie(col
                .column("nrOfWords", DataTypes.integerType()))
                .setLabel(messageSource.getMessage("report.word.statistics.total",
                        new Object[]{totalWords}, Locale.getDefault()));
    }

    private static TextColumnBuilder<String> getCategory() {
        return col.column("day", DataTypes.stringType());
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Month getMonth() {
        return month;
    }

    public String getPath() {
        return filePath;
    }
}
