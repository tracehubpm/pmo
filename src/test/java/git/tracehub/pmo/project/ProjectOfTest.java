/*
 * Copyright (c) 2023-2024 Tracehub
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test suite for {@link ProjectOf}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class ProjectOfTest {

    /**
     * Result set.
     */
    @Mock
    private ResultSet set;

    @Test
    void returnsProjectWithAllFields() throws SQLException {
        final Project expected = new Project(
            UUID.randomUUID(),
            "Test Project",
            "Location",
            "Description",
            true
        );
        Mockito.when(this.set.getString("id"))
            .thenReturn(expected.getId().toString());
        Mockito.when(this.set.getString("name"))
            .thenReturn(expected.getName());
        Mockito.when(this.set.getString("location"))
            .thenReturn(expected.getLocation());
        Mockito.when(this.set.getString("description"))
            .thenReturn(expected.getDescription());
        Mockito.when(this.set.getBoolean("active"))
            .thenReturn(expected.isActive());
        final Project project = new ProjectOf(this.set).value();
        MatcherAssert.assertThat(
            "Project %s is null".formatted(project),
            project,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Project %s isn't correct".formatted(project),
            project,
            new IsEqual<>(expected)
        );
    }

}
