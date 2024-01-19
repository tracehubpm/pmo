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

import java.io.InputStream;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;

/**
 * SQL Statement.
 *
 * @since 0.0.0
 */
public final class SqlStatement implements Sql {

    /**
     * InputStream.
     */
    private final InputStream input;

    /**
     * Ctor.
     *
     * @param path Path
     * @throws Exception if something went wrong
     */
    public SqlStatement(final String path) throws Exception {
        this(new ResourceOf("sql/%s".formatted(path)).stream());
    }

    /**
     * Ctor.
     *
     * @param stream InputStream
     */
    public SqlStatement(final InputStream stream) {
        this.input = stream;
    }

    @Override
    public String asString() throws Exception {
        return new TextOf(
            this.input
        ).asString();
    }
}
