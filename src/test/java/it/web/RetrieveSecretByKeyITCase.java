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

package it.web;

import com.jcabi.http.Request;
import com.jcabi.http.Response;
import com.jcabi.http.request.JdkRequest;
import git.tracehub.pmo.PmoApplication;
import git.tracehub.pmo.controller.SecretController;
import io.github.eocqrs.eokson.MutableJson;
import it.KeycloakIntegration;
import it.PostgresIntegration;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration Test Case for Retrieving Secret by Key
 * from {@link SecretController}.
 *
 * @since 0.0.0
 */
@ActiveProfiles("pgit")
@SpringBootTest(
    classes = PmoApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@SuppressWarnings("JTCOP.RuleAllTestsHaveProductionClass")
final class RetrieveSecretByKeyITCase
    implements KeycloakIntegration, PostgresIntegration {

    /**
     * Raw Endpoint.
     */
    private static final String RAW = "http://localhost:%s/secrets";

    /**
     * Application Port.
     */
    @LocalServerPort
    private int port;

    @Test
    @Sql("classpath:pre/sql/projects.sql")
    void retrievesSecretByKeySuccessfully() throws Exception {
        final Response response = new JdkRequest(
            RetrieveSecretByKeyITCase.RAW.formatted(this.port)
        ).uri()
            .queryParam("project", UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"))
            .queryParam("name", "key")
            .back()
            .method(Request.GET)
            .header(
                HttpHeaders.AUTHORIZATION,
                "Bearer %s".formatted(
                    new KeycloakToken(
                        KeycloakIntegration.KEYCLOAK.getAuthServerUrl()
                    ).value()
                )
            ).fetch();
        MatcherAssert.assertThat(
            "Response Status %s does not match to expected one".formatted(
                response.status()
            ),
            response.status(),
            new IsEqual<>(200)
        );
    }

    @Test
    void throwsOnInvalidKey() throws Exception {
        final UUID project = UUID.randomUUID();
        final String key = "key";
        final Response response = new JdkRequest(
            RetrieveSecretByKeyITCase.RAW.formatted(this.port)
        ).uri()
            .queryParam("project", project)
            .queryParam("name", key)
            .back()
            .method(Request.GET)
            .header(
                HttpHeaders.AUTHORIZATION,
                "Bearer %s".formatted(
                    new KeycloakToken(
                        KeycloakIntegration.KEYCLOAK.getAuthServerUrl()
                    ).value()
                )
            ).fetch();
        MatcherAssert.assertThat(
            "Response Status %s does not match to expected one".formatted(
                response.status()
            ),
            response.status(),
            new IsEqual<>(404)
        );
        MatcherAssert.assertThat(
            "Message %s isn't correct".formatted(response.body()),
            response.body(),
            new IsEqual<>(
                new MutableJson()
                    .with(
                        "message",
                        "Secret with project = %s and key = %s not found"
                            .formatted(project, key)
                    ).toString()
            )
        );
    }

}
