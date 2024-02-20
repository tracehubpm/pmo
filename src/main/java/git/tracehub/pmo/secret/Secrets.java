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

import java.util.List;
import java.util.UUID;
import org.cactoos.Scalar;

/**
 * Secrets.
 *
 * @since 0.0.0
 */
public interface Secrets {

    /**
     * List all keys from the project.
     *
     * @param project Project id
     * @return List of keys
     */
    List<Secret> keys(UUID project);

    /**
     * Value by key.
     *
     * @param project Project id
     * @param key Key
     * @return Secret
     */
    Secret value(UUID project, String key);

    /**
     * Create a secret.
     *
     * @param secret Secret
     * @return Secret
     */
    Secret create(Scalar<Secret> secret);

    /**
     * Update secret.
     *
     * @param secret Secret
     * @return Secret
     */
    Secret update(Scalar<Secret> secret);

    /**
     * Is secret exists.
     *
     * @param project Project id
     * @param key Key
     * @return Result
     */
    boolean exists(UUID project, String key);

}
