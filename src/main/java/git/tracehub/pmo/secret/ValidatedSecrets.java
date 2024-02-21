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

import git.tracehub.pmo.exception.ResourceAlreadyExistsException;
import git.tracehub.pmo.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
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
     * Constructor.
     *
     * @param origin Secrets
     */
    public ValidatedSecrets(@Qualifier("defaultSecrets") final Secrets origin) {
        this.origin = origin;
    }

    @Override
    public List<Secret> keys(final UUID project) {
        return this.origin.keys(project);
    }

    @Override
    public Secret value(final UUID project, final String key) {
        return this.origin.value(project, key);
    }

    @Override
    @SneakyThrows
    public Secret create(final Scalar<Secret> secret) {
        final Secret content = secret.value();
        if (this.origin.exists(content.getProject(), content.getKey())) {
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
        if (!this.origin.exists(content.getProject(), content.getKey())) {
            throw new ResourceNotFoundException(
                "Secret with project = %s and key = %s not found"
                    .formatted(secret.value().getProject(), secret.value().getKey())
            );
        }
        return this.origin.update(secret);
    }

    @Override
    public boolean exists(final UUID project, final String key) {
        return this.origin.exists(project, key);
    }

}
