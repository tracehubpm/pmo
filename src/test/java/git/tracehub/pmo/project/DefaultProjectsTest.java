/*
 * Copyright (c) 2023-2024 Tracehub.git
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package git.tracehub.pmo.project;

import git.tracehub.pmo.database.JdbcTest;
import git.tracehub.pmo.exception.ResourceNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test suite for {@link DefaultProjects}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class DefaultProjectsTest extends JdbcTest {

    /**
     * Default tickets.
     */
    @InjectMocks
    private DefaultProjects projects;

    @Test
    void returnsProjectById() throws SQLException {
        final Project expected = new Project(
            UUID.randomUUID(),
            "Test Project",
            "Location",
            "Description",
            true
        );
        super.setProject(expected);
        Mockito.when(super.set.next()).thenReturn(true);
        final Project project = this.projects.byId(expected.getId());
        MatcherAssert.assertThat(
            "Project %s is null".formatted(project),
            true,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Project %s isn't correct".formatted(project),
            project,
            new IsEqual<>(expected)
        );
    }

    @Test
    void returnsProjectsByUser() throws SQLException {
        final String email = "email";
        final Project expected = new Project(
            UUID.randomUUID(),
            "Test Project",
            "Location",
            "Description",
            true
        );
        super.setProject(expected);
        Mockito.when(super.set.next()).thenReturn(true, false);
        final List<Project> actual = this.projects.byUser(email);
        MatcherAssert.assertThat(
            "List of projects %s is null".formatted(actual),
            true,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "List of projects %s isn't correct".formatted(actual),
            actual,
            Matchers.contains(expected)
        );
    }

    @Test
    void employsProject() throws SQLException {
        final Project expected = new Project(
            UUID.randomUUID(),
            "Test Project",
            "Location",
            "Description",
            true
        );
        super.setProject(expected);
        Mockito.when(super.set.next()).thenReturn(true);
        final Project project = this.projects.employ(expected);
        MatcherAssert.assertThat(
            "Project %s is null".formatted(project),
            true,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Project %s isn't correct".formatted(project),
            project,
            new IsEqual<>(expected)
        );
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnInvalidProjectId() throws SQLException {
        final UUID id = UUID.randomUUID();
        Mockito.when(super.set.next()).thenReturn(false);
        new Assertion<>(
            "Exception is not thrown or valid",
            () -> this.projects.byId(id),
            new Throws<>(
                "Project %s not found".formatted(id),
                ResourceNotFoundException.class
            )
        ).affirm();
    }

}
