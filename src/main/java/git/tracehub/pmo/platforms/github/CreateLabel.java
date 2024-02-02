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

import com.jcabi.github.Labels;
import com.jcabi.github.Repo;
import git.tracehub.pmo.platforms.Action;
import git.tracehub.pmo.platforms.ColorInHex;
import java.awt.Color;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Create label in Github.
 *
 * @checkstyle IllegalTokenCheck (40 lines)
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class CreateLabel implements Action {

    /**
     * Github repository.
     */
    private final Repo repo;

    /**
     * Names of labels.
     */
    private final List<String> names;

    /**
     * Colors of labels.
     */
    private final List<Color> colors;

    @Override
    @SneakyThrows
    public void exec() {
        final Labels labels = this.repo.labels();
        for (int index = 0; index < this.names.size(); index++) {
            final String name = this.names.get(index);
            final boolean exists = StreamSupport.stream(
                labels.iterate().spliterator(),
                false
            ).anyMatch(label -> name.equals(label.name()));
            if (!exists) {
                labels.create(
                    name,
                    new ColorInHex(this.colors.get(index)).value()
                );
            }
        }
    }

}
