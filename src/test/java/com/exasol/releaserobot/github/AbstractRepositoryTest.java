package com.exasol.releaserobot.github;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.kohsuke.github.*;
import org.mockito.Mockito;

import com.exasol.releaserobot.repository.RepositoryException;
import com.exasol.releaserobot.usecases.Repository;

class AbstractRepositoryTest {
    @Test
    // [utest->dsn~repository-retrieves-branch-content~1]
    void testCreateAbstractGitHubGitRepositoryContentWithInvalidBranch() throws IOException {
        final GHRepository ghRepositoryMock = Mockito.mock(GHRepository.class);
        final String branchName = "my_branch";
        when(ghRepositoryMock.getBranch(branchName)).thenThrow(IOException.class);
        final Optional<String> latestTag = Optional.of("1.2.8");
        assertThrows(RepositoryException.class,
                () -> new DummyRepository(ghRepositoryMock, branchName, latestTag, ""));
    }

    @Test
    // [utest->dsn~repository-retrieves-branch-content~1]
    void testIsDefaultBranchTrue() throws IOException {
        final GHRepository ghRepositoryMock = Mockito.mock(GHRepository.class);
        final GHBranch branchMock = Mockito.mock(GHBranch.class);
        final String branchName = "my_branch";
        when(ghRepositoryMock.getBranch(branchName)).thenReturn(branchMock);
        when(ghRepositoryMock.getDefaultBranch()).thenReturn(branchName);
        when(branchMock.getName()).thenReturn(branchName);
        final Repository repository = new DummyRepository(ghRepositoryMock, branchName, Optional.of("1.0.0"), "name");
        assertThat(repository.isOnDefaultBranch(), equalTo(true));
    }

    @Test
    // [utest->dsn~repository-retrieves-branch-content~1]
    void testIsDefaultBranchFalse() throws IOException {
        final GHRepository ghRepositoryMock = Mockito.mock(GHRepository.class);
        final GHBranch branchMock = Mockito.mock(GHBranch.class);
        final String branchName = "my_branch";
        when(ghRepositoryMock.getBranch(branchName)).thenReturn(branchMock);
        when(ghRepositoryMock.getDefaultBranch()).thenReturn("main");
        when(branchMock.getName()).thenReturn(branchName);
        final Repository repository = new DummyRepository(ghRepositoryMock, branchName, Optional.of("1.0.0"), "name");
        assertThat(repository.isOnDefaultBranch(), equalTo(false));
    }

    @Test
    void testGetChangelogFile() throws IOException {
        final GHRepository ghRepositoryMock = Mockito.mock(GHRepository.class);
        final GHContent contentMock = Mockito.mock(GHContent.class);
        final GHBranch branchMock = Mockito.mock(GHBranch.class);
        final String branchName = "my_branch";
        final String textContent = "Text content";
        when(contentMock.read()).thenReturn(new ByteArrayInputStream(textContent.getBytes()));
        when(ghRepositoryMock.getBranch(branchName)).thenReturn(branchMock);
        when(branchMock.getName()).thenReturn(branchName);
        when(ghRepositoryMock.getFileContent(anyString(), anyString())).thenReturn(contentMock);
        final Repository repository = new DummyRepository(ghRepositoryMock, branchName, Optional.of("1.0.0"), "name");
        assertThat(repository.getChangelogFile(), equalTo(textContent));
    }

    @Test
    void testGetChangelogFileThrowsException() throws IOException {
        final GHRepository ghRepositoryMock = Mockito.mock(GHRepository.class);
        final GHBranch branchMock = Mockito.mock(GHBranch.class);
        final String branchName = "my_branch";
        when(ghRepositoryMock.getBranch(branchName)).thenReturn(branchMock);
        when(branchMock.getName()).thenReturn(branchName);
        when(ghRepositoryMock.getFileContent(anyString(), anyString())).thenThrow(IOException.class);
        final Repository repository = new DummyRepository(ghRepositoryMock, branchName, Optional.of("1.0.0"), "name");
        assertThrows(RepositoryException.class, repository::getChangelogFile);
    }

    @Test
    void testGetChangesFileWithCaching() throws IOException {
        final GHRepository ghRepositoryMock = Mockito.mock(GHRepository.class);
        final GHContent contentMock = Mockito.mock(GHContent.class);
        final GHBranch branchMock = Mockito.mock(GHBranch.class);
        final String branchName = "my_branch";
        when(contentMock.read()).thenReturn(new ByteArrayInputStream("".getBytes()));
        when(ghRepositoryMock.getBranch(branchName)).thenReturn(branchMock);
        when(branchMock.getName()).thenReturn(branchName);
        when(ghRepositoryMock.getFileContent(anyString(), anyString())).thenReturn(contentMock);
        final Repository repository = new DummyRepository(ghRepositoryMock, branchName, Optional.of("1.0.0"), "name");
        assertAll(
                () -> assertThat(repository.getReleaseLetter(repository.getVersion()).getFileName(),
                        equalTo("changes_1.0.0.md")),
                () -> assertThat(repository.getReleaseLetter(repository.getVersion()).getFileName(),
                        equalTo("changes_1.0.0.md")),
                () -> verify(ghRepositoryMock, times(1)).getFileContent(anyString(), anyString()));
    }

    private static final class DummyRepository extends Repository {
        protected DummyRepository(final GHRepository repository, final String branch, final Optional<String> latestTag,
                final String fullName) {
            super(repository, branch, fullName, latestTag);
        }

        @Override
        public String getVersion() {
            return "1.0.0";
        }

        @Override
        public Map<String, String> getDeliverables() {
            return null;
        }
    }
}