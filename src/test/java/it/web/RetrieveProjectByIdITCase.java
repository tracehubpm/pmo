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

package it.web;

import com.jcabi.http.Request;
import com.jcabi.http.Response;
import com.jcabi.http.request.JdkRequest;
import git.tracehub.pmo.PmoApplication;
import git.tracehub.pmo.controller.ProjectController;
import io.github.eocqrs.eokson.MutableJson;
import it.KeycloakIntegration;
import it.PostgresIntegration;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration Test Case for Retrieving Project by Id
 * from {@link ProjectController}.
 *
 * @since 0.0.0
 */
@ActiveProfiles("pgit")
@SpringBootTest(
    classes = PmoApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@SuppressWarnings("JTCOP.RuleAllTestsHaveProductionClass")
final class RetrieveProjectByIdITCase
    implements KeycloakIntegration, PostgresIntegration {

    /**
     * Raw Endpoint.
     */
    private static final String RAW = "http://localhost:%s/projects/%s";

    /**
     * Application Port.
     */
    @LocalServerPort
    private int port;

    @Test
    @Sql("classpath:pre/sql/projects.sql")
    void retrievesProjectByIdSuccessfully() throws Exception {
        final Response response = new JdkRequest(
            RetrieveProjectByIdITCase.RAW.formatted(
                this.port,
                "74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"
            )
        ).method(Request.GET)
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
    void throwsOnInvalidProjectId() throws Exception {
        final Response response = new JdkRequest(
            RetrieveProjectByIdITCase.RAW.formatted(
                this.port,
                "22bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"
            )
        ).method(Request.GET)
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
            new IsEqual<>(403)
        );
        MatcherAssert.assertThat(
            "Message %s isn't correct".formatted(response.body()),
            response.body(),
            new IsEqual<>(
                new MutableJson()
                    .with("message", "Access Denied")
                    .toString()
            )
        );
    }

}
