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

import git.tracehub.pmo.exception.ResourceNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test suite for {@link DefaultSecrets}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class DefaultSecretsTest {

    /**
     * Result set.
     */
    @Mock
    private ResultSet set;

    /**
     * Datasource.
     */
    @Mock
    private DataSource source;

    /**
     * Connection.
     */
    @Mock
    private Connection connection;

    /**
     * Statement.
     */
    @Mock
    private PreparedStatement statement;

    /**
     * Default secrets.
     */
    @InjectMocks
    private DefaultSecrets secrets;

    /**
     * Set datasource and connection.
     *
     * @throws SQLException If something goes wrong
     */
    @BeforeEach
    void setConnection() throws SQLException {
        Mockito.when(this.source.getConnection()).thenReturn(this.connection);
        Mockito.doNothing().when(this.connection).setAutoCommit(true);
        Mockito.lenient().when(this.connection.prepareStatement(Mockito.anyString()))
            .thenReturn(this.statement);
        Mockito.lenient()
            .when(this.connection.prepareStatement(Mockito.anyString(), Mockito.anyInt()))
            .thenReturn(this.statement);
        Mockito.doNothing().when(this.statement).close();
        Mockito.lenient().when(this.statement.executeQuery()).thenReturn(this.set);
        Mockito.lenient().when(this.statement.getGeneratedKeys()).thenReturn(this.set);
    }

    @Test
    void returnsValueByKey() throws SQLException {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "key",
            "value"
        );
        new MockSecret(this.set).exec(expected);
        Mockito.when(this.set.next()).thenReturn(true);
        final Secret secret = this.secrets.value(
            new Key(
                expected.getProject(),
                expected.getKey()
            )
        );
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
    void updatesSecret() throws SQLException {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "unique key",
            "unique value"
        );
        new MockSecret(this.set).exec(expected);
        Mockito.when(this.set.next()).thenReturn(true);
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
    void createsSecret() throws SQLException {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "key",
            "value"
        );
        new MockSecret(this.set).exec(expected);
        Mockito.when(this.set.next()).thenReturn(true);
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
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnInvalidKey() throws SQLException {
        final Key key = new Key(UUID.randomUUID(), "invalid");
        Mockito.when(this.set.next()).thenReturn(false);
        new Assertion<>(
            "Exception is not thrown",
            () -> this.secrets.value(key),
            new Throws<>(
                "404 NOT_FOUND \"Secret with project = %s and key = %s not found\""
                    .formatted(key.getProject(), key.getName()),
                ResourceNotFoundException.class
            )
        ).affirm();
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnCreatingInvalidSecret() throws SQLException {
        Mockito.when(this.set.next()).thenThrow(SQLException.class);
        new Assertion<>(
            "Exception is not thrown or valid",
            () -> this.secrets.create(
                () -> new Secret(
                    UUID.randomUUID(),
                    "test key",
                    "test value"
                )
            ),
            new Throws<>(SQLException.class)
        ).affirm();
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnUpdatingInvalidSecret() throws SQLException {
        Mockito.when(this.set.next()).thenThrow(SQLException.class);
        new Assertion<>(
            "Exception is not thrown or valid",
            () -> this.secrets.update(
                () -> new Secret(
                    UUID.randomUUID(),
                    "test key",
                    "test value"
                )
            ),
            new Throws<>(SQLException.class)
        ).affirm();
    }

}
