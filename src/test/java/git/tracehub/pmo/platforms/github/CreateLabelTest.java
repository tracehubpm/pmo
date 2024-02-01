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

package git.tracehub.pmo.platforms.github;

import com.jcabi.github.Repo;
import com.jcabi.github.Repos;
import com.jcabi.github.mock.MkGithub;
import java.awt.Color;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link CreateLabel}.
 *
 * @since 0.0.0
 */
final class CreateLabelTest {

    @Test
    void createsLabelSuccessfully() throws IOException {
        final String label = "new";
        final MkGithub github = new MkGithub("user");
        final Repo repo = github.repos().create(
            new Repos.RepoCreate("repo", false)
        );
        new CreateLabel(github, "user/repo", label, Color.BLUE).exec();
        MatcherAssert.assertThat(
            "Label %s isn't created".formatted(label),
            repo.labels().get(label).name(),
            new IsEqual<>(label)
        );
    }

    @Test
    void trowsOnInvalidLocation() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new CreateLabel(
                new MkGithub("user"),
                "user/repo",
                "label",
                Color.BLUE
            ).exec(),
            "Exception is not thrown or valid"
        );
    }

}
