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

package it.database;

import git.tracehub.pmo.PmoApplication;
import git.tracehub.pmo.project.DefaultProjects;
import git.tracehub.pmo.project.Project;
import git.tracehub.pmo.project.Projects;
import it.PostgresIntegration;
import java.util.List;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration tests for {@link DefaultProjects}.
 *
 * @since 0.0.0
 */
@ActiveProfiles("pgit")
@SpringBootTest(classes = PmoApplication.class)
@SuppressWarnings("JTCOP.RuleAllTestsHaveProductionClass")
final class DefaultProjectsIT implements PostgresIntegration {

    /**
     * Projects.
     */
    @Autowired
    private Projects projects;

    @Test
    @Sql("classpath:pre/sql/projects.sql")
    void returnsProjectById() {
        final Project expected = new Project(
            UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"),
            "Test",
            "github@user/test:master",
            "Description",
            true
        );
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
    @Sql("classpath:pre/sql/projects.sql")
    void returnsProjectsByUser() {
        final String email = "user";
        final Project expected = new Project(
            UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"),
            "Test",
            "github@user/test:master",
            "Description",
            true
        );
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
    void employsProject() {
        final Project expected = new Project(
            UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"),
            "New project",
            "Location",
            "Description",
            true
        );
        final Project project = this.projects.employ(expected);
        MatcherAssert.assertThat(
            "Project %s is null".formatted(project),
            true,
            Matchers.notNullValue()
        );
    }

}
