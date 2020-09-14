package com.exasol.repository;

import java.util.Map;

/**
 * This class represents Git repository content based on the latest commit of the user-specified branch.
 */
public interface GitBranchContent {
    /**
     * Get a changelog file as a string.
     *
     * @return changelog file as a string
     */
    public String getChangelogFile();

    /**
     * Get a changes file as an instance of {@link ReleaseLetter}.
     *
     * @param version version as a string
     * @return release changes file
     */
    public ReleaseLetter getReleaseLetter(final String version);

    /**
     * Get a current project version.
     *
     * @return version as a string
     */
    // [impl->dsn~gr-provides-current-version~1]
    public String getVersion();

    /**
     * Get key-value pairs for deliverable names and corresponding deliverable pathes.
     * 
     * @return map with deliverables information
     */
    public Map<String, String> getDeliverables();
}