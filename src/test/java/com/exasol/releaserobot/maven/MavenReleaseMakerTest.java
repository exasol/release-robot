package com.exasol.releaserobot.maven;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.exasol.releaserobot.ReleaseMaker;
import com.exasol.releaserobot.github.GitHubException;
import com.exasol.releaserobot.github.GithubGateway;
import com.exasol.releaserobot.maven.release.MavenReleaseMaker;
import com.exasol.releaserobot.repository.GitBranchContent;

class MavenReleaseMakerTest {
    @Test
    void testMakeReleaseShouldSucceed() {
        final GithubGateway githubGateway = mock(GithubGateway.class);
        final GitBranchContent contentMock = Mockito.mock(GitBranchContent.class);
        when(contentMock.getBranchName()).thenReturn("main");
        final ReleaseMaker releaseMaker = new MavenReleaseMaker(contentMock, githubGateway);
        assertAll(() -> assertDoesNotThrow(releaseMaker::makeRelease),
                () -> verify(githubGateway, times(1)).sendGitHubRequest(any(), anyString()));
    }

    @Test
    void testMakeReleaseShouldFail() throws GitHubException {
        final GithubGateway githubGateway = mock(GithubGateway.class);
        final GitBranchContent contentMock = Mockito.mock(GitBranchContent.class);
        when(contentMock.getBranchName()).thenReturn("main");
        final ReleaseMaker releaseMaker = new MavenReleaseMaker(contentMock, githubGateway);
        doThrow(GitHubException.class).when(githubGateway).sendGitHubRequest(any(), anyString());
        assertAll(() -> assertThrows(GitHubException.class, releaseMaker::makeRelease),
                () -> verify(githubGateway, times(1)).sendGitHubRequest(any(), anyString()));
    }
}
