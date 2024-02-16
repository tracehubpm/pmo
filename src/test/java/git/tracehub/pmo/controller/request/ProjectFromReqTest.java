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

package git.tracehub.pmo.controller.request;

import git.tracehub.pmo.project.Project;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ProjectFromReq}.
 *
 * @since 0.0.0
 */
final class ProjectFromReqTest {

    @Test
    void createsProjectFromRequest() {
        final Project expected = new Project(
            UUID.randomUUID(),
            "Test Project",
            "Location",
            "Description",
            true
        );
        final Project project = new ProjectFromReq(
            new RqProject(
                expected.getName(),
                expected.getLocation(),
                expected.getDescription(),
                expected.isActive()
            )
        ).value();
        MatcherAssert.assertThat(
            "Project %s is null".formatted(project),
            true,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Project %s isn't correct".formatted(project),
            project,
            Matchers.samePropertyValuesAs(expected, "id")
        );
    }

}
