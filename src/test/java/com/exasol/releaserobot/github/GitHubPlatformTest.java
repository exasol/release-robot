package com.exasol.releaserobot.github;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GitHubPlatformTest {
    @Test
    void testReleaseThrowsException() throws IOException, GitHubException {
        final GithubGateway githubGateway = Mockito.mock(GithubGateway.class);
        final GitHubRelease release = GitHubRelease.builder().version("1.0.0").header("header").releaseLetter("")
                .assets(Map.of("assets", "path")).build();
        when(githubGateway.createGithubRelease(release)).thenThrow(GitHubException.class);
        final GitHubPlatform platform = new GitHubPlatform(githubGateway);
        assertAll(() -> assertThrows(GitHubException.class, () -> platform.makeNewGitHubRelease(release)),
                () -> verify(githubGateway, times(1)).createGithubRelease(release));
    }

    @Test
    void testGetClosedTickets() throws GitHubException {
        final GithubGateway githubGateway = Mockito.mock(GithubGateway.class);
        when(githubGateway.getClosedTickets()).thenReturn(Set.of(24, 31));
        final GitHubPlatform platform = new GitHubPlatform(githubGateway);
        assertThat(platform.getClosedTickets(), equalTo(Set.of(24, 31)));
    }

    @Test
    void testGetClosedTicketsThrowsException() throws GitHubException {
        final GithubGateway githubGateway = Mockito.mock(GithubGateway.class);
        when(githubGateway.getClosedTickets()).thenThrow(GitHubException.class);
        final GitHubPlatform platform = new GitHubPlatform(githubGateway);
        assertThrows(IllegalStateException.class, platform::getClosedTickets);
    }
}