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

import lombok.SneakyThrows;
import org.cactoos.Scalar;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Encrypted secrets.
 *
 * @checkstyle DesignForExtensionCheck (80 lines)
 * @since 0.0.0
 */
@Component
public class EncryptedSecrets implements Secrets {

    /**
     * Secrets.
     */
    private final Secrets origin;

    /**
     * Encryptor.
     */
    private final TextEncryptor encryptor;

    /**
     * Constructor.
     *
     * @param origin Secrets
     * @param encryptor Encryptor
     */
    public EncryptedSecrets(
        @Qualifier("validatedSecrets") final Secrets origin,
        final TextEncryptor encryptor
    ) {
        this.origin = origin;
        this.encryptor = encryptor;
    }

    @Override
    public Secret value(final Key key) {
        final Secret secret = this.origin.value(key);
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
        return this.origin.create(
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
        return this.origin.update(
            () -> new Secret(
                content.getProject(),
                content.getKey(),
                this.encryptor.encrypt(content.getValue())
            )
        );
    }

}
