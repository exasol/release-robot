package com.exasol.github;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.github.*;

import com.exasol.git.GitRepositoryContent;
import com.exasol.git.ReleaseChangesLetter;

/**
 * Contains common logic for GitHub-based repositories' content.
 */
public abstract class AbstractGitHubGitRepositoryContent implements GitRepositoryContent {
    private static final String CHANGELOG_FILE_PATH = "doc/changes/changelog.md";
    private final GHRepository repository;
    private final GHBranch branch;
    private final Map<String, ReleaseChangesLetter> releaseChangesLetters = new HashMap<>();

    /**
     * Create a new instance of {@link AbstractGitHubGitRepositoryContent}.
     *
     * @param repository an instance of {@link GHRepository}
     * @param branch name of a branch to get content from
     */
    protected AbstractGitHubGitRepositoryContent(final GHRepository repository, final String branch) {
        this.repository = repository;
        this.branch = getBranchByName(branch);
    }

    private GHBranch getBranchByName(final String branch) {
        try {
            return this.repository.getBranch(branch);
        } catch (final IOException exception) {
            throw new GitHubException(
                    "Cannot find a branch '" + branch + "'. Please check if you specified a correct branch.",
                    exception);
        }
    }

    /**
     * Get the content of a file in this repository.
     *
     * @param filePath path of the file as a string
     * @return content as a string
     */
    protected String getSingleFileContentAsString(final String filePath) {
        try {
            final GHContent content = this.repository.getFileContent(filePath, this.branch.getName());
            return content.getContent();
        } catch (final IOException exception) {
            throw new GitHubException("Cannot find or read the file '" + filePath + "' in the repository "
                    + this.repository.getName() + ". Please add this file according to the User Guide.", exception);
        }
    }

    @Override
    public final String getChangelogFile() {
        return getSingleFileContentAsString(CHANGELOG_FILE_PATH);
    }

    @Override
    public final synchronized ReleaseChangesLetter getReleaseChangesLetter(final String version) {
        if (!this.releaseChangesLetters.containsKey(version)) {
            final String fileName = "changes_" + version + ".md";
            final String filePath = "doc/changes/" + fileName;
            final String fileContent = getSingleFileContentAsString(filePath);
            this.releaseChangesLetters.put(version, new ReleaseChangesLetter(fileName, fileContent));
        }
        return this.releaseChangesLetters.get(version);
    }
}