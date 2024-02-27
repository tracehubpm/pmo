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

package git.tracehub.pmo.secret;

import git.tracehub.pmo.exception.ResourceAlreadyExistsException;
import git.tracehub.pmo.exception.ResourceNotFoundException;
import lombok.SneakyThrows;
import org.cactoos.Scalar;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Validated secrets.
 *
 * @checkstyle DesignForExtensionCheck (70 lines)
 * @since 0.0.0
 */
@Component
public class ValidatedSecrets implements Secrets {

    /**
     * Secrets.
     */
    private final Secrets origin;

    /**
     * Keys.
     */
    private final Keys keys;

    /**
     * Constructor.
     *
     * @param origin Secrets
     * @param keys Keys
     */
    public ValidatedSecrets(
        @Qualifier("defaultSecrets") final Secrets origin,
        final Keys keys
    ) {
        this.origin = origin;
        this.keys = keys;
    }

    @Override
    public Secret value(final Key key) {
        return this.origin.value(key);
    }

    @Override
    @SneakyThrows
    public Secret create(final Scalar<Secret> secret) {
        final Secret content = secret.value();
        if (this.keys.exists(new Key(content.getProject(), content.getKey()))) {
            throw new ResourceAlreadyExistsException(
                "Secret with project = %s and key = %s already exists"
                    .formatted(secret.value().getProject(), secret.value().getKey())
            );
        }
        return this.origin.create(secret);
    }

    @Override
    @SneakyThrows
    public Secret update(final Scalar<Secret> secret) {
        final Secret content = secret.value();
        if (!this.keys.exists(new Key(content.getProject(), content.getKey()))) {
            throw new ResourceNotFoundException(
                "Secret with project = %s and key = %s not found"
                    .formatted(secret.value().getProject(), secret.value().getKey())
            );
        }
        return this.origin.update(secret);
    }

}
