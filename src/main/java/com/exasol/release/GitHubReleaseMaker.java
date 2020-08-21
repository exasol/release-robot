package com.exasol.release;

import java.util.logging.Logger;

import com.exasol.git.GitRepository;
import com.exasol.git.GitRepositoryContent;
import com.exasol.github.GitHubGitRepository;

/**
 * This class responds for releases on GitHub
 */
public class GitHubReleaseMaker implements ReleaseMaker {
    private static final Logger LOGGER = Logger.getLogger(GitHubReleaseMaker.class.getName());
    private final GitRepository repository;

    /**
     * Create a new {@link GitHubReleaseMaker}.
     * 
     * @param repository an instance of {@link GitHubGitRepository}
     */
    public GitHubReleaseMaker(final GitRepository repository) {
        this.repository = repository;
    }

    @Override
    public void makeRelease() {
        LOGGER.fine("Releasing on GitHub.");
        final GitRepositoryContent content = this.repository
                .getRepositoryContent(this.repository.getDefaultBranchName());
        final String version = content.getVersion();
        final String changes = content.getChangesFile(version);
        this.repository.release(version, changes);
    }
}