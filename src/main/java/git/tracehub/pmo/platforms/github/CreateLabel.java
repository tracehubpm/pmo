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
import git.tracehub.pmo.platforms.Label;
import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.Lombok;
import lombok.RequiredArgsConstructor;

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
     * List of labels.
     */
    private final List<Label> labels;

    @Override
    public void exec() {
        final Labels items = this.repo.labels();
        this.labels.forEach(
            label -> {
                final boolean absent = StreamSupport.stream(
                    items.iterate().spliterator(),
                    false
                ).noneMatch(item -> item.name().equals(label.getName()));
                if (absent) {
                    try {
                        items.create(
                            label.getName(),
                            new ColorInHex(label.getColor()).value()
                        );
                    } catch (final IOException ex) {
                        throw Lombok.sneakyThrow(ex);
                    }
                }
            }
        );
    }

}
