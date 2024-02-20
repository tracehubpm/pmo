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
import lombok.SneakyThrows;
import org.cactoos.Scalar;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Encrypted secrets.
 *
 * @checkstyle DesignForExtensionCheck (70 lines)
 * @since 0.0.0
 */
@Component
public class EncryptedSecrets implements Secrets {

    /**
     * Secrets.
     */
    private final Secrets secrets;

    /**
     * Encryptor.
     */
    private final TextEncryptor encryptor;

    /**
     * Constructor.
     *
     * @param secrets Secrets
     * @param encryptor Encryptor
     */
    public EncryptedSecrets(
        @Qualifier("validatedSecrets") final Secrets secrets,
        final TextEncryptor encryptor
    ) {
        this.secrets = secrets;
        this.encryptor = encryptor;
    }

    @Override
    public List<Secret> keys(final UUID project) {
        return this.secrets.keys(project);
    }

    @Override
    public Secret value(final UUID project, final String key) {
        final Secret secret = this.secrets.value(project, key);
        return new Secret(
            secret.getProject(),
            secret.getKey(),
            this.encryptor.decrypt(secret.getValue())
        );
    }

    @Override
    @SneakyThrows
    public Secret create(final Scalar<Secret> secret) {
        final Secret content = secret.value();
        return this.secrets.create(
            () -> new Secret(
                content.getProject(),
                content.getKey(),
                this.encryptor.encrypt(content.getValue())
            )
        );
    }

    @Override
    @SneakyThrows
    public Secret update(final Scalar<Secret> secret) {
        final Secret content = secret.value();
        return this.secrets.update(
            () -> new Secret(
                content.getProject(),
                content.getKey(),
                this.encryptor.encrypt(content.getValue())
            )
        );
    }

    @Override
    public boolean exists(final UUID project, final String key) {
        return this.secrets.exists(project, key);
    }

}