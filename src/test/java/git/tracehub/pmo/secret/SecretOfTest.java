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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test suite for {@link SecretOf}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class SecretOfTest {

    /**
     * Result set.
     */
    @Mock
    private ResultSet set;

    @Test
    void returnsSecretWithAllFields() throws SQLException {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "key",
            "value"
        );
        Mockito.when(this.set.getString("project"))
            .thenReturn(expected.getProject().toString());
        Mockito.when(this.set.getString("key"))
            .thenReturn(expected.getKey());
        Mockito.when(this.set.getString("value"))
            .thenReturn(expected.getValue());
        final Secret secret = new SecretOf(this.set).value();
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
    void throwsOnInvalidResultSet() throws SQLException {
        Mockito.when(this.set.getString("project"))
            .thenThrow(SQLException.class);
        new Assertion<>(
            "Exception is not thrown or valid",
            () ->  new SecretOf(this.set).value(),
            new Throws<>(SQLException.class)
        ).affirm();
    }

}
