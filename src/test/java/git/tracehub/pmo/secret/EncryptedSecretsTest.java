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

import java.io.IOException;
import java.util.UUID;
import org.cactoos.Scalar;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.jasypt.util.text.TextEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test suite for {@link ValidatedSecrets}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class EncryptedSecretsTest {

    /**
     * Secrets.
     */
    @Mock
    private Secrets origin;

    /**
     * Encryptor.
     */
    @Mock
    private TextEncryptor encryptor;

    /**
     * Encrypted secrets.
     */
    @InjectMocks
    private EncryptedSecrets secrets;

    @Test
    void returnsValueByKey() {
        final Key key = new Key(UUID.randomUUID(), "key");
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "key",
            "value"
        );
        Mockito.when(this.origin.value(key))
            .thenReturn(expected);
        Mockito.when(this.encryptor.decrypt(expected.getValue()))
            .thenReturn(expected.getValue());
        final Secret secret = this.secrets.value(key);
        MatcherAssert.assertThat(
            "Secret %s is null".formatted(secret),
            secret,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Secret %s isn't correct".formatted(secret),
            secret,
            new IsEqual<>(expected)
        );
    }

    @Test
    void createsSecret() {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "unique key",
            "unique value"
        );
        Mockito.when(this.origin.create(Mockito.any(Scalar.class)))
            .thenReturn(expected);
        final Secret secret = this.secrets.create(() -> expected);
        MatcherAssert.assertThat(
            "Secret %s isn't created".formatted(secret),
            secret,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Secret %s isn't correct".formatted(secret),
            secret,
            new IsEqual<>(expected)
        );
    }

    @Test
    void updatesSecret() {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "key",
            "value"
        );
        Mockito.when(this.origin.update(Mockito.any(Scalar.class)))
            .thenReturn(expected);
        final Secret secret = this.secrets.update(() -> expected);
        MatcherAssert.assertThat(
            "Secret %s isn't updated".formatted(secret),
            secret,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Secret %s isn't correct".formatted(secret),
            secret,
            new IsEqual<>(expected)
        );
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnInvalidSecretForCreating() {
        new Assertion<>(
            "Exception is not thrown",
            () -> this.secrets.create(
                () -> {
                    throw new IOException();
                }
            ),
            new Throws<>(IOException.class)
        ).affirm();
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnInvalidSecretForUpdating() {
        new Assertion<>(
            "Exception is not thrown",
            () -> this.secrets.update(
                () -> {
                    throw new IOException();
                }
            ),
            new Throws<>(IOException.class)
        ).affirm();
    }

}
