/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023-2024 Tracehub.git
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package git.tracehub.pmo.security.expression;

import git.tracehub.pmo.project.Project;
import git.tracehub.pmo.project.Projects;
import java.util.ArrayList;
import java.util.UUID;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test suite for {@link HasProject}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class HasProjectTest {

    /**
     * Users.
     */
    @Mock
    private Projects projects;

    @Test
    void checksIsUserHasAccessToProject() {
        final Project project = new Project(
            UUID.randomUUID(),
            "Test",
            "Location",
            "Description",
            true
        );
        final String user = "email";
        new Authentication(user).authenticate();
        Mockito.when(this.projects.byUser(user)).thenReturn(new ListOf<>(project));
        MatcherAssert.assertThat(
            "User %s hasn't project %s".formatted(user, project.getId()),
            new HasProject(this.projects).validate(project.getId()),
            new IsEqual<>(true)
        );
    }

    @Test
    void checksIsUserHasNotAccessToProject() {
        final UUID id =  UUID.randomUUID();
        final String user = "email";
        new Authentication(user).authenticate();
        Mockito.when(this.projects.byUser(user)).thenReturn(new ArrayList<>(0));
        MatcherAssert.assertThat(
            "User %s has project %s".formatted(user, id),
            new HasProject(this.projects).validate(id),
            new IsEqual<>(false)
        );
    }

}
