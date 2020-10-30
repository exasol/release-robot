package com.exasol.releaserobot.release;

import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.exasol.releaserobot.MavenPlatform;
import com.exasol.releaserobot.github.GitHubException;
import com.exasol.releaserobot.report.ReleaseReport;
import com.exasol.releaserobot.repository.GitBranchContent;

/**
 * This class is responsible for releases on Maven Central.
 */
public class MavenReleaseMaker implements ReleaseMaker {
    private static final Logger LOGGER = Logger.getLogger(MavenReleaseMaker.class.getName());
    private final GitBranchContent content;
    private final MavenPlatform platform;
    private final ReleaseReport releaseReport;

    /**
     * Create a new instance of {@link MavenReleaseMaker}.
     *
     * @param content       repository content
     * @param platform      instance of {@link MavenPlatform}
     * @param releaseReport instance of {@link ReleaseReport}
     */
    public MavenReleaseMaker(final GitBranchContent content, final MavenPlatform platform,
            final ReleaseReport releaseReport) {
        this.content = content;
        this.platform = platform;
        this.releaseReport = releaseReport;
    }

    @Override
    public boolean makeRelease() {
        LOGGER.fine("Releasing on Maven.");
        try {
            this.platform.makeNewMavenRelease(this.content.getBranchName());
            this.releaseReport.addSuccessfulRelease(this.platform.getPlatformName());
            return true;
        } catch (final GitHubException exception) {
            this.releaseReport.addFailedRelease(this.platform.getPlatformName(),
                    ExceptionUtils.getStackTrace(exception));
            return false;
        }
    }
}