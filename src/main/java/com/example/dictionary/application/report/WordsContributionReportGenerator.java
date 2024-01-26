package com.example.dictionary.application.report;

import com.example.dictionary.application.i18n.LocaleConfig;
import com.example.dictionary.application.report.data.WordDetail;
import com.example.dictionary.domain.entity.User;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.io.Serial;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.dictionary.application.report.util.ReportUtils.COLUMN_SPACE;
import static com.example.dictionary.application.report.util.ReportUtils.DD_MM_YYYY;
import static com.example.dictionary.application.report.util.ReportUtils.MARGIN;
import static com.example.dictionary.application.report.util.ReportUtils.filePath;
import static com.example.dictionary.application.report.util.ReportUtils.getDetailStyle;
import static com.example.dictionary.application.report.util.ReportUtils.getFooterComponents;
import static com.example.dictionary.application.report.util.ReportUtils.getOutputStream;
import static com.example.dictionary.application.report.util.ReportUtils.getPageFooterStyle;
import static com.example.dictionary.application.report.util.ReportUtils.getPageHeader;
import static com.example.dictionary.application.report.util.ReportUtils.getPageHeaderStyle;
import static com.example.dictionary.application.report.util.ReportUtils.getSubreportColumnStyle;
import static com.example.dictionary.application.report.util.ReportUtils.getSubreportSummary;
import static com.example.dictionary.application.report.util.ReportUtils.getSubreportTitleStyle;
import static com.example.dictionary.application.report.util.ReportUtils.getTitle;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.variable;

public class WordsContributionReportGenerator implements ReportGenerator {

    private List<WordDetail> words;

    private Map<User, List<WordDetail>> userWords;

    private List<User> users;

    private final String currentUser;

    private final MessageSource messageSource;

    public WordsContributionReportGenerator(List<WordDetail> words, String currentUser) {
        LocaleConfig localeConfig = new LocaleConfig();
        this.messageSource = localeConfig.messageSource();
        this.words = words;
        this.userWords = words.stream()
                .collect(Collectors.groupingBy(WordDetail::getContributor));
        this.currentUser = currentUser;
        this.users = userWords.keySet().stream().toList();
    }

    public void generate() throws IOException, DRException {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
                .setDataSource(new SubreportDataSourceExpression());
        report()
                .setLocale(Locale.getDefault())
                .title(getTitle(messageSource
                        .getMessage("report.user.contributions.title", null, Locale.getDefault())))
                .detail(subreport)
                .setDataSource(createDataSource())
                .setPageMargin(margin(MARGIN))
                .setPageFormat(PageType.A4)
                .setDetailStyle(getDetailStyle())
                .pageFooter(getFooterComponents())
                .pageHeader(getPageHeader(currentUser, messageSource))
                .setPageHeaderStyle(getPageHeaderStyle())
                .setPageFooterStyle(getPageFooterStyle())
                .toPdf(getOutputStream(messageSource.getMessage(
                        "report.user.contributions.file.name",
                        null, Locale.getDefault())));
    }

    private JRDataSource createDataSource() {
        return new JREmptyDataSource(userWords.size());
    }

    private class SubreportExpression extends AbstractSimpleExpression<JasperReportBuilder> {
        private static final long serialVersionUID = 1L;

        @Override
        public JasperReportBuilder evaluate(ReportParameters reportParameters) {
            int masterRowNumber = reportParameters.getReportRowNumber();
            JasperReportBuilder report = report();
            User user = users.get(masterRowNumber - 1);

            TextColumnBuilder<String> name = col.column("name", type.stringType());
            TextColumnBuilder<String> category = col.column("category", type.stringType());
            TextColumnBuilder<String> addedAt = col.column(new DateColumn(DD_MM_YYYY));

            VariableBuilder<Long> subtotal = variable(name, Calculation.COUNT);
            TextFieldBuilder<String> summarySubtotal = cmp.text(new CustomTextSubtotal(subtotal, messageSource));
            TextFieldBuilder<String> title = cmp.text(user.getFirstName() + " " + user.getLastName())
                    .setStyle(stl.style().setFont(stl.font()).bold());
            report
                    .title(title)
                    .setTitleStyle(getSubreportTitleStyle())
                    .fields(field("addedAt", DataTypes.dateType()))
                    .variables(subtotal)
                    .addColumn(
                            name,
                            category,
                            addedAt)
                    .setHighlightDetailEvenRows(true)
                    .setColumnStyle(getSubreportColumnStyle())
                    .setPageColumnSpace(COLUMN_SPACE)
                    .summary(getSubreportSummary(summarySubtotal));

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {
        private static final long serialVersionUID = 1L;

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            int masterRowNumber = reportParameters.getReportRowNumber();

            return new JRBeanCollectionDataSource(userWords.get(users.get(masterRowNumber - 1)));
        }
    }

    private static class CustomTextSubtotal extends AbstractSimpleExpression<String> {

        private final VariableBuilder<Long> subtotal;

        private final MessageSource messageSource;

        public CustomTextSubtotal(VariableBuilder<Long> subtotal, MessageSource messageSource) {
            this.subtotal = subtotal;
            this.messageSource = messageSource;
        }

        @Override
        public String evaluate(ReportParameters reportParameters) {
            return messageSource.getMessage("report.user.contributions.total",
                    new Object[]{type.longType().valueToString(subtotal, reportParameters)},
                    Locale.getDefault());
        }
    }


    public static class DateColumn extends AbstractSimpleExpression<String> {

        @Serial
        private static final long serialVersionUID = 1L;

        private final String pattern;

        public DateColumn(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String evaluate(ReportParameters reportParameters) {
            LocalDateTime value = reportParameters.getValue("addedAt");
            return DateTimeFormatter.ofPattern(pattern).format(value);
        }
    }

    public String getPath() {
        return filePath;
    }
}


