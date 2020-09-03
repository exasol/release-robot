package com.exasol.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.exasol.Platform;
import com.exasol.github.GitHubPlatform;

class PlatformValidatorFactoryTest {
    @Test
    void testCreateProjectValidatorGitHub() {
        final GitHubPlatform platform = Mockito.mock(GitHubPlatform.class);
        when(platform.getPlatformName()).thenReturn(Platform.PlatformName.GITHUB);
        assertThat(PlatformValidatorFactory.createPlatformValidator(null, platform),
                instanceOf(GitHubPlatformValidator.class));
    }

    @Test
    void testCreateProjectValidatorUnsupported() {
        final GitHubPlatform platform = Mockito.mock(GitHubPlatform.class);
        when(platform.getPlatformName()).thenReturn(Platform.PlatformName.MAVEN);
        assertThrows(UnsupportedOperationException.class,
                () -> PlatformValidatorFactory.createPlatformValidator(null, platform));
    }
}