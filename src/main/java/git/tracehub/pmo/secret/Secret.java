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

package git.tracehub.pmo.secret;

import java.util.UUID;
import lombok.Data;

/**
 * Key and value pair.
 *
 * @checkstyle ParameterNumberCheck (90 lines)
 * @since 0.0.0
 */
@Data
public class Secret {

    /**
     * Id.
     */
    private UUID id;

    /**
     * Project id.
     */
    private final UUID project;

    /**
     * Key.
     */
    private final String key;

    /**
     * Value.
     */
    private final String value;

    /**
     * Constructor.
     *
     * @param id Id
     * @param project Project id
     * @param key Key
     * @param value Value
     */
    public Secret(
        final UUID id,
        final UUID project,
        final String key,
        final String value
    ) {
        this.id = id;
        this.project = project;
        this.key = key;
        this.value = value;
    }

    /**
     * Constructor.
     *
     * @param project Project id
     * @param key Key
     * @param value Value
     */
    public Secret(
        final UUID project,
        final String key,
        final String value
    ) {
        this(null, project, key, value);
    }

}
