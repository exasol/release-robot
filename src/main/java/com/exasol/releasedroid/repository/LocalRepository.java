package com.exasol.releasedroid.repository;

import static org.eclipse.jgit.lib.Constants.R_TAGS;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

import com.exasol.errorreporting.ExaError;

/**
 * This class represents a local repository.
 */
// [impl->dsn~local-repository~1]
public class LocalRepository extends BaseRepository {
    private final String localPath;

    /**
     * Create a new instance of {@link LocalRepository}.
     * 
     * @param localPath      path to a root of a local repository
     * @param repositoryName name of a repository
     */
    public LocalRepository(final String localPath, final String repositoryName) {
        super(repositoryName);
        this.localPath = localPath;
    }

    @Override
    public String getSingleFileContentAsString(final String filePath) {
        final Path path = Paths.get(this.localPath, filePath);
        try {
            return Files.readString(path);
        } catch (final IOException exception) {
            throw new RepositoryException(ExaError.messageBuilder("E-RR-REP-1")
                    .message("Cannot read a file from the local repository: {{path}}.")
                    .parameter("path", this.localPath + filePath)
                    .mitigation("Please check that the file exists and the local path is correct").toString());
        }
    }

    @Override
    public void updateFileContent(final String filePath, final String newContent, final String commitMessage) {
        throw new UnsupportedOperationException(ExaError.messageBuilder("E-RR-REP-8")
                .message("File updates are not currently supported on a local repository.").toString());
    }

    @Override
    // We assume that there is no a default branch in a local repository as a default branch in our case means a branch
    // on which we create a new tag and start a release process. And a release now is only implemented on the GitHub.
    public boolean isOnDefaultBranch() {
        return false;
    }

    @Override
    public String getBranchName() {
        final File rootDirectory = new File(this.localPath);
        try (final Git git = Git.open(rootDirectory)) {
            return git.getRepository().getBranch();
        } catch (final IOException exception) {
            throw new RepositoryException(ExaError.messageBuilder("E-RR-REP-6")
                    .message("Cannot retrieve a name of a local git branch.").toString());
        }
    }

    @Override
    public Optional<String> getLatestTag() {
        final File rootDirectory = new File(this.localPath);
        try (final Git git = Git.open(rootDirectory)) {
            final List<Ref> tags = git.getRepository().getRefDatabase().getRefsByPrefix(R_TAGS);
            if (tags.isEmpty()) {
                return Optional.empty();
            } else {
                final Ref ref = tags.get(tags.size() - 1);
                return Optional.of(ref.getName().replace("refs/tags/", ""));
            }
        } catch (final IOException exception) {
            throw new RepositoryException(ExaError.messageBuilder("E-RR-REP-7")
                    .message("Cannot retrieve a name of a local git branch.").toString());
        }
    }
}