package com.example.dictionary.application.report;

import com.example.dictionary.application.facade.UserFacade;
import com.example.dictionary.application.report.data.WordDetail;
import com.example.dictionary.domain.entity.User;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.CurrentDateBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.PageNumberBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.BorderBuilder;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serial;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.dictionary.ui.util.UiUtils.APP_NAME;
import static java.awt.Color.BLACK;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import static net.sf.dynamicreports.report.builder.DynamicReports.variable;
import static net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER;
import static net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT;
import static net.sf.dynamicreports.report.constant.LineStyle.SOLID;

public class WordsReport {

    private List<WordDetail> words;

    private Map<User, List<WordDetail>> userWords;

    private List<User> users;

    public static final String CURRENT_DATE_PATTERN = "EEEEE dd MMMMM";

    private final UserFacade userFacade;

    private String currentUser;

    public WordsReport(List<WordDetail> words, UserFacade userFacade) {
        this.words = words;
        this.userWords = words.stream()
                .collect(Collectors.groupingBy(WordDetail::getContributor));
        this.userFacade = userFacade;
        this.users = userWords.keySet().stream().toList();
        currentUser = this.userFacade.getUserProfile().getFirstName() + " " + this.userFacade.getUserProfile().getLastName();
    }

    public void generate() {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
                .setDataSource(new SubreportDataSourceExpression());

        try {
            report()
                    .title(cmp.text("USER WORD CONTRIBUTIONS REPORT")
                            .setStyle(stl
                                    .style()
                                    .setFontSize(12)
                                    .bold()
                                    .setForegroundColor(new Color(1, 1, 43)))
                            .setHorizontalTextAlignment(CENTER)
                            .setHeight(40))
                    .detail(subreport)
                    .setDataSource(createDataSource())
                    .setPageMargin(margin(50))
                    .setPageFormat(PageType.A4)
                    .setDetailStyle(stl.style().setBorder(getBorderBuilder()))
                    .pageFooter(getFooterComponents())
                    .pageHeader(cmp.horizontalList(cmp.text(APP_NAME),
                            cmp.text("Reported by " + currentUser).setHorizontalTextAlignment(RIGHT)))
                    .setPageHeaderStyle(stl.style().setBottomPadding(30))
                    .setPageFooterStyle(stl.style().setTopPadding(30))
                    .toPdf(new FileOutputStream("src/main/resources/report.pdf"));
        } catch (DRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            TextColumnBuilder<String> addedAt = col.column(new DateColumn("dd-MM-yyyy"));

            VariableBuilder<Long> subtotal = variable(name, Calculation.COUNT);
            TextFieldBuilder<String> summarySubtotal = cmp.text(new CustomTextSubtotal(subtotal));
            TextFieldBuilder<String> title = cmp.text(user.getFirstName() + " " + user.getLastName())
                    .setStyle(stl.style().setFont(stl.font()).bold());
            report
                    .title(title)
                    .setTitleStyle(stl.style()
                            .bold()
                            .setBackgroundColor(new Color(204, 229, 255))
                            .setLeftPadding(25)
                            .setBottomPadding(10)
                            .setTopPadding(10)
                            .setBorder(getBorderBuilder()))
                    .fields(field("addedAt", DataTypes.dateType()))
                    .variables(subtotal)
                    .addColumn(
                            name,
                            category,
                            addedAt)
                    .setHighlightDetailEvenRows(true)
                    .setColumnStyle(stl.style().setLeftPadding(60))
                    .setPageColumnSpace(10)
                    .summary(summarySubtotal.setStyle(
                            stl.style()
                                    .setHorizontalTextAlignment(RIGHT)
                                    .setRightPadding(40)
                                    .setBottomPadding(10)
                                    .setTopPadding(10)
                                    .setFont(stl.font().italic())
                                    .setBorder(getBorderBuilder())
                    ));

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

        public CustomTextSubtotal(VariableBuilder<Long> subtotal) {
            this.subtotal = subtotal;
        }

        @Override
        public String evaluate(ReportParameters reportParameters) {
            return "Total word contributions: " + type.longType().valueToString(subtotal, reportParameters);
        }
    }

    private static class DateColumn extends AbstractSimpleExpression<String> {

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

    private static BorderBuilder getBorderBuilder() {
        return stl
                .border(getPenBuilder());
    }

    private static PenBuilder getPenBuilder() {
        return stl
                .pen(0.1f, SOLID)
                .setLineColor(BLACK);
    }

    private static HorizontalListBuilder getFooterComponents() {
        return cmp
                .horizontalList(
                        getCurrentDate(),
                        getPageXofY()
                );
    }


    private static CurrentDateBuilder getCurrentDate() {
        return cmp
                .currentDate()
                .setPattern(CURRENT_DATE_PATTERN);
    }

    private static PageNumberBuilder getPageXofY() {
        return cmp
                .pageNumber()
                .setHorizontalTextAlignment(RIGHT);
    }

    private static TextFieldBuilder<String> getPageTextComponent() {
        return cmp
                .text("Page")
                .setHorizontalTextAlignment(RIGHT);
    }
}


