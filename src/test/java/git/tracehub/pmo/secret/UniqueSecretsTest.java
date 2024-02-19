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
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test suite for {@link UniqueSecrets}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class UniqueSecretsTest {

    /**
     * Secrets.
     */
    @Mock
    private Secrets origin;

    /**
     * Unique secrets.
     */
    @InjectMocks
    private UniqueSecrets secrets;

    @Test
    void returnsValueByKey() {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "key",
            "value"
        );
        Mockito.when(this.origin.value(expected.getProject(), expected.getKey()))
            .thenReturn(expected);
        final Secret secret = this.secrets.value(
            expected.getProject(),
            expected.getKey()
        );
        MatcherAssert.assertThat(
            "Secret %s is null".formatted(secret),
            true,
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
            "key",
            "value"
        );
        Mockito.when(this.origin.create(Mockito.any())).thenReturn(expected);
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
    void existsSecret() {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "key",
            "value"
        );
        Mockito.when(this.origin.exists(expected.getProject(), expected.getKey()))
            .thenReturn(false);
        MatcherAssert.assertThat(
            "Secret %s exists".formatted(expected),
            this.secrets.exists(expected.getProject(), expected.getKey()),
            new IsEqual<>(false)
        );
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnIDuplicate() {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "key",
            "value"
        );
        Mockito.when(this.origin.exists(expected.getProject(), expected.getKey()))
            .thenReturn(true);
        new Assertion<>(
            "Exception is not thrown or valid",
            () -> this.secrets.create(() -> expected),
            new Throws<>(
                "Secret with project = %s and key = %s already exists"
                    .formatted(expected.getProject(), expected.getKey()),
                ResourceAlreadyExistsException.class
            )
        ).affirm();
    }

}
