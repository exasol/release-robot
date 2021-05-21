package com.exasol.releasedroid.formatting;

import static com.exasol.releasedroid.usecases.ReleaseDroidConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import com.exasol.releasedroid.usecases.report.ReleaseReport;
import com.exasol.releasedroid.usecases.report.Report;
import com.exasol.releasedroid.usecases.report.ValidationReport;
import com.exasol.releasedroid.usecases.request.PlatformName;

class ReportLoggerFormatterTest {
    private final ReportLoggerFormatter formatter = new ReportLoggerFormatter();

    @Test
    void testFormatReleaseReport() {
        final Report outer = ReleaseReport.create();
        final Report github = ReleaseReport.create(PlatformName.GITHUB);
        github.addSuccessfulResult("Release finished.");
        final Report maven = ReleaseReport.create(PlatformName.MAVEN);
        maven.addSuccessfulResult("Release finished.");
        outer.merge(github);
        outer.merge(maven);
        final String formattedReport = this.formatter.formatReport(outer);
        assertThat(formattedReport, equalTo(ANSI_GREEN + "Release was performed without any problems!" + ANSI_RESET));
    }

    @Test
    void testFormatReleaseReportWithFailures() {
        final Report outer = ReleaseReport.create();
        final Report github = ReleaseReport.create(PlatformName.GITHUB);
        github.addSuccessfulResult("Release finished.");
        final Report maven = ReleaseReport.create(PlatformName.MAVEN);
        maven.addFailedResult("Wrong credentials.");
        outer.merge(github);
        outer.merge(maven);
        final String formattedReport = this.formatter.formatReport(outer);
        assertThat(formattedReport, equalTo("RELEASE FAILED!" + LINE_SEPARATOR + LINE_SEPARATOR
                + "Wrong credentials. [For platforms: MAVEN]" + LINE_SEPARATOR));
    }

    @Test
    void testFormatValidationReport() {
        final Report outer = ValidationReport.create();
        final Report github = ValidationReport.create(PlatformName.GITHUB);
        github.addSuccessfulResult("Date is correct!");
        final Report maven = ValidationReport.create(PlatformName.MAVEN);
        maven.addSuccessfulResult("Plugins are here!");
        outer.merge(github);
        outer.merge(maven);
        final String formattedReport = this.formatter.formatReport(outer);
        assertThat(formattedReport,
                equalTo(ANSI_GREEN + "Validation was performed without any problems!" + ANSI_RESET));
    }

    @Test
    void testFormatValidationReportWithFailures() {
        final Report outer = ValidationReport.create();
        final Report github = ValidationReport.create(PlatformName.GITHUB);
        github.addFailedResult("Date is wrong!");
        github.addSuccessfulResult("Changelog is fine!");
        final Report maven = ValidationReport.create(PlatformName.MAVEN);
        maven.addFailedResult("Date is wrong!");
        maven.addFailedResult("Some plugins are missing!");
        outer.merge(github);
        outer.merge(maven);
        final String formattedReport = this.formatter.formatReport(outer);
        assertThat(formattedReport,
                equalTo("VALIDATION FAILED!" + LINE_SEPARATOR + LINE_SEPARATOR
                        + "Date is wrong! [For platforms: GITHUB,MAVEN]" + LINE_SEPARATOR
                        + "Some plugins are missing! [For platforms: MAVEN]" + LINE_SEPARATOR));
    }
}