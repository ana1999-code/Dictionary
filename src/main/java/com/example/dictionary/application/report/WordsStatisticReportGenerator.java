package com.example.dictionary.application.report;

import com.example.dictionary.application.exception.ValueRequiredException;
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

import java.io.IOException;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.dictionary.application.report.util.ReportUtils.BLUE;
import static com.example.dictionary.application.report.util.ReportUtils.MARGIN;
import static com.example.dictionary.application.report.util.ReportUtils.WORD_STATISTICS;
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

    public WordsStatisticReportGenerator(List<Word> words, String currentUser) {
        this.words = words;
        this.currentUser = currentUser;
    }

    public void generate() throws IOException, DRException {
        totalWords = 0;
        report()
                .title(getTitle("WORD STATISTICS FOR " + month + " " + year))
                .setDataSource(getDataSource())
                .setPageMargin(margin(MARGIN))
                .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                .summary(getBarChartBuilder())
                .pageFooter(getFooterComponents())
                .pageHeader(getPageHeader(currentUser))
                .setPageHeaderStyle(getPageHeaderStyle())
                .setPageFooterStyle(getPageFooterStyle())
                .toPdf(getOutputStream(WORD_STATISTICS));
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
                .setTitle("Words Added Per Month")
                .seriesColors(BLUE)
                .setCategory(getCategory())
                .series(getSeries())
                .setCategoryAxisFormat(getCategoryAxisFormat())
                .setValueAxisFormat(getValueAxisFormat());
    }

    private static AxisFormatBuilder getValueAxisFormat() {
        return cht.axisFormat().setLabel("Number of Words");
    }

    private static AxisFormatBuilder getCategoryAxisFormat() {
        return cht.axisFormat().setLabel("Day");
    }

    private CategoryChartSerieBuilder getSeries() {
        return serie(col
                .column("nrOfWords", DataTypes.integerType()))
                .setLabel("Total words: " + totalWords);
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

    public String getPath() {
        return filePath;
    }
}
