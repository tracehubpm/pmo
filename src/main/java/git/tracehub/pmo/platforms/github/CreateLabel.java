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

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Labels;
import git.tracehub.pmo.platforms.Action;
import git.tracehub.pmo.platforms.ColorInHex;
import java.awt.Color;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Create label in Github.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class CreateLabel implements Action {

    /**
     * Github.
     */
    private final Github github;

    /**
     * Owner and repo.
     */
    private final String location;

    /**
     * Label name.
     */
    private final String name;

    /**
     * Label color.
     */
    private final Color color;

    @Override
    @SneakyThrows
    public void exec() {
        final Labels labels = this.github.repos()
            .get(new Coordinates.Simple(this.location))
            .labels();
        final boolean exists = StreamSupport.stream(
            labels.iterate().spliterator(),
            false
        ).anyMatch(label -> this.name.equals(label.name()));
        if (!exists) {
            labels.create(this.name, new ColorInHex(this.color).value());
        }
    }

}
