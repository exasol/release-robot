package com.exasol.releasedroid.formatting;

import static com.exasol.releasedroid.usecases.ReleaseDroidConstants.LINE_SEPARATOR;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.releasedroid.usecases.logging.ReportFormatter;
import com.exasol.releasedroid.usecases.report.Report;
import com.exasol.releasedroid.usecases.request.PlatformName;
import com.exasol.releasedroid.usecases.request.UserInput;

public class SummaryFormatter {
    private final ReportFormatter reportFormatter;

    public SummaryFormatter(final ReportFormatter reportFormatter) {
        this.reportFormatter = reportFormatter;
    }

    public String formatResponse(final UserInput userInput, final List<PlatformName> platformNames,
            final List<Report> reports) {
        return formatInputUser(userInput, platformNames) + formatReports(reports);
    }

    private String formatInputUser(final UserInput userInput, final List<PlatformName> platformNames) {
        final String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        final var stringBuilder = new StringBuilder();
        stringBuilder.append(println(now));
        stringBuilder.append(println(""));
        stringBuilder.append(println("Goal: " + userInput.getGoal()));
        stringBuilder.append(println("Repository: " + userInput.getFullRepositoryName()));
        stringBuilder.append(
                println("Platforms: " + platformNames.stream().map(Enum::name).collect(Collectors.joining(", "))));
        if (userInput.hasBranch()) {
            stringBuilder.append(println("Git branch: " + userInput.getBranch()));
        }
        stringBuilder.append(println(""));
        return stringBuilder.toString();
    }

    private String println(final String string) {
        return string + LINE_SEPARATOR;
    }

    private String formatReports(final List<Report> reports) {
        final var stringBuilder = new StringBuilder();
        for (final Report report : reports) {
            stringBuilder.append(this.reportFormatter.formatReport(report));
            stringBuilder.append(LINE_SEPARATOR);
        }
        return stringBuilder.toString();
    }
}