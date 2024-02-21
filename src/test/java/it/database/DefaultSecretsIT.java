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

package it.database;

import git.tracehub.pmo.PmoApplication;
import git.tracehub.pmo.secret.DefaultSecrets;
import git.tracehub.pmo.secret.Key;
import git.tracehub.pmo.secret.Secret;
import git.tracehub.pmo.secret.Secrets;
import it.PostgresIntegration;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration tests for {@link DefaultSecrets}.
 *
 * @since 0.0.0
 */
@ActiveProfiles("pgit")
@SpringBootTest(classes = PmoApplication.class)
@SuppressWarnings("JTCOP.RuleAllTestsHaveProductionClass")
final class DefaultSecretsIT implements PostgresIntegration {

    /**
     * Secrets.
     */
    @Autowired
    @Qualifier("defaultSecrets")
    private Secrets secrets;

    @Test
    @Sql("classpath:pre/sql/projects.sql")
    void returnsValueByKey() {
        final Secret expected = new Secret(
            UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"),
            "key",
            "Iohe7UjU5MjHcxqsKk9vMLTlYHFCXHCLl4q0EjfBbfzmiYtXI1Vfw3KRUQEmbeVh"
        );
        final Secret secret = this.secrets.value(
            new Key(
                UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"),
                "key"
            )
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
    @Sql("classpath:pre/sql/projects.sql")
    void createsSecret() {
        final Secret expected = new Secret(
            UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"),
            "new key",
            "value"
        );
        final Secret secret = this.secrets.create(() -> expected);
        MatcherAssert.assertThat(
            "Secret %s is null".formatted(secret),
            true,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Secret %s isn't correct".formatted(secret),
            secret,
            Matchers.samePropertyValuesAs(expected, "id")
        );
    }

}
