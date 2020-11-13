package com.exasol.releaserobot.repository.maven;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kohsuke.github.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.releaserobot.repository.RepositoryException;
import com.exasol.releaserobot.usecases.Repository;

@ExtendWith(MockitoExtension.class)
class MavenRepositoryTest {
    private static final String BRANCH_NAME = "my_branch";
    @Mock
    private GHRepository ghRepositoryMock;
    @Mock
    private GHContent contentMock;
    @Mock
    private GHBranch branchMock;

    @BeforeEach
    void beforeEach() throws IOException {
        when(this.ghRepositoryMock.getBranch(BRANCH_NAME)).thenReturn(this.branchMock);
        when(this.branchMock.getName()).thenReturn(BRANCH_NAME);
        when(this.ghRepositoryMock.getFileContent(anyString(), anyString())).thenReturn(this.contentMock);
    }

    @ParameterizedTest
    @ValueSource(strings = { "<project><version>1.0.0</version><artifactId>project</artifactId></project>", //
            "<project>\n<version>\n1.0.0\n</version>\n<artifactId>project</artifactId></project>",
            "<project>    <version>  1.0.0  </version> <artifactId>project</artifactId>   </project>" })
    // [utest->dsn~repository-provides-current-version~1]
    void testGetVersionWithCaching(final String pomFile) throws IOException {
        final Repository repository = createRepository(pomFile);
        assertAll(() -> assertThat(repository.getVersion(), equalTo("1.0.0")),
                () -> assertThat(repository.getVersion(), equalTo("1.0.0")),
                () -> verify(this.ghRepositoryMock, times(1)).getFileContent(anyString(), anyString()));
    }

    private Repository createRepository(final String pomFile) throws IOException {
        when(this.contentMock.read()).thenReturn(new ByteArrayInputStream(pomFile.getBytes()));
        return new MavenRepository(this.ghRepositoryMock, BRANCH_NAME, "", Optional.empty());
    }

    @Test
    // [utest->dsn~repository-provides-deliverables-information~1]
    void testGetDeliverables() throws IOException {
        final String pomFile = "<project><version>1.0.0</version><artifactId>project</artifactId></project>";
        final Repository repository = createRepository(pomFile);
        assertThat(repository.getDeliverables(), equalTo(Map.of("project-1.0.0.jar", "./target/project-1.0.0.jar")));
    }

    @Test
    // [utest->dsn~repository-provides-deliverables-information~1]
    void testGetDeliverablesWithPluginInformation() throws IOException {
        final String pom = "<project>" //
                + "    <artifactId>my-test-project</artifactId>" //
                + "    <version>1.2.3</version>" //
                + "    <properties>" //
                + "        <vscjdbc.version>5.0.4</vscjdbc.version>" //
                + "    </properties>" //
                + "    <build>" //
                + "        <plugins>" //
                + "            <plugin>" //
                + "                <artifactId>maven-assembly-plugin</artifactId>" //
                + "                 <configuration>" //
                + "                    <finalName>virtual-schema-dist-${vscjdbc.version}-bundle-${version}</finalName>"
                + "                </configuration>" //
                + "            </plugin>" //
                + "        </plugins>" //
                + "    </build>" //
                + "</project>";
        final Repository repository = createRepository(pom);
        assertThat(repository.getDeliverables(), equalTo(Map.of("virtual-schema-dist-5.0.4-bundle-1.2.3.jar",
                "./target/virtual-schema-dist-5.0.4-bundle-1.2.3.jar")));
    }

    @Test
    // [utest->dsn~repository-provides-deliverables-information~1]
    void testGetDeliverablesFails() throws IOException {
        final String pom = "<project>" //
                + "    <artifactId>my-test-project</artifactId>" //
                + "    <version>1.2.3</version>" //
                + "    <properties>" //
                + "    </properties>" //
                + "    <build>" //
                + "        <plugins>" //
                + "            <plugin>" //
                + "                <artifactId>maven-assembly-plugin</artifactId>" //
                + "                 <configuration>" //
                + "                    <finalName>virtual-schema-dist-${vscjdbc.version}-bundle-${version}</finalName>"
                + "                </configuration>" //
                + "            </plugin>" //
                + "        </plugins>" //
                + "    </build>" //
                + "</project>";
        final Repository repository = createRepository(pom);
        final IllegalStateException exception = assertThrows(IllegalStateException.class, repository::getDeliverables);
        assertThat(exception.getMessage(), containsString("F-POM-2"));
    }

    @Test
    void testGetVersionInvalidPom() throws IOException {
        final String pom = "nothing here";
        when(this.contentMock.read()).thenReturn(new ByteArrayInputStream(pom.getBytes()));
        final Optional<String> latestTag = Optional.empty();
        assertThrows(RepositoryException.class,
                () -> new MavenRepository(this.ghRepositoryMock, BRANCH_NAME, "", latestTag));
    }
}