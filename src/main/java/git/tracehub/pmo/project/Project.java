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

package git.tracehub.pmo.project;

import java.util.UUID;
import lombok.Data;

/**
 * Project.
 *
 * @checkstyle ParameterNumberCheck (70 lines)
 * @since 0.0.0
 */
@Data
public class Project {

    /**
     * Id.
     */
    private UUID id;

    /**
     * Name.
     */
    private final String name;

    /**
     * Location.
     */
    private final String location;

    /**
     * Description.
     */
    private final String description;

    /**
     * Is active.
     */
    private final boolean active;

    /**
     * Constructor.
     *
     * @param id Id
     * @param name Name
     * @param location Location
     * @param description Description
     * @param active Is active
     */
    public Project(
        final UUID id,
        final String name,
        final String location,
        final String description,
        final boolean active
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.active = active;
    }

    /**
     * Constructor.
     *
     * @param name Name
     * @param location Location
     * @param description Description
     * @param active Is active
     */
    public Project(
        final String name,
        final String location,
        final String description,
        final boolean active
    ) {
        this(null, name, location, description, active);
    }
}
