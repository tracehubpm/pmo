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

package it.platforms.github;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;
import git.tracehub.pmo.platforms.Label;
import git.tracehub.pmo.platforms.github.CreateLabels;
import java.awt.Color;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link CreateLabels}.
 *
 * @since 0.0.0
 */
final class CreateLabelsIT {

    @Test
    void createsLabelSuccessfully() {
        final String label = "new";
        final Repo repo = new RtGithub(
            System.getProperty("GithubToken")
        ).repos()
            .get(
                new Coordinates.Simple("hizmailovich/draft")
            );
        new CreateLabels(repo, new ListOf<>(new Label(label, Color.RED)))
            .exec();
        MatcherAssert.assertThat(
            "Label %s isn't created".formatted(label),
            repo.labels().get(label).name(),
            new IsEqual<>(label)
        );
    }

}
