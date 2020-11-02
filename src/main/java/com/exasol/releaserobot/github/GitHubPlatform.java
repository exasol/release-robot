package com.exasol.releaserobot.github;

import com.exasol.releaserobot.*;
import com.exasol.releaserobot.report.ValidationReport;

/**
 * This class controls GitHub platform.
 */
public class GitHubPlatform implements Platform {
    private final ReleaseMaker releaseMaker;
    private final PlatformValidator platformValidator;

    /**
     * Create a new instance of {@link GitHubPlatform}.
     * 
     * @param releaseMaker      instance of {@link ReleaseMaker}
     * @param platformValidator instance of {@link PlatformValidator}
     */
    protected GitHubPlatform(final ReleaseMaker releaseMaker, final PlatformValidator platformValidator) {
        this.releaseMaker = releaseMaker;
        this.platformValidator = platformValidator;
    }

    @Override
    public void release(final UserInput userInput) throws GitHubException {
        this.releaseMaker.makeRelease();
    }

    @Override
    public PlatformName getPlatformName() {
        return PlatformName.GITHUB;
    }

    @Override
    public void validate(final ValidationReport validationReport) {
        this.platformValidator.validate(validationReport);
    }
}