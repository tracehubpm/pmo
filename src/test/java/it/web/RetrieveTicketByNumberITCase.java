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
import git.tracehub.pmo.controller.TicketController;
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
 * Integration Test Case for Retrieving Ticket by Number and Repository
 * from {@link TicketController}.
 *
 * @since 0.0.0
 */
@ActiveProfiles("pgit")
@SpringBootTest(
    classes = PmoApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@SuppressWarnings("JTCOP.RuleAllTestsHaveProductionClass")
final class RetrieveTicketByNumberITCase
    implements KeycloakIntegration, PostgresIntegration {

    /**
     * Raw Endpoint.
     */
    private static final String RAW = "http://localhost:%s/tickets";

    /**
     * Application Port.
     */
    @LocalServerPort
    private int port;

    @Test
    @Sql("classpath:pre/sql/projects.sql")
    void retrievesTicketByIssueNumberSuccessfully() throws Exception {
        final Response response = new JdkRequest(
            RetrieveTicketByNumberITCase.RAW.formatted(this.port)
        ).uri()
            .queryParam("number", 1)
            .queryParam("repo", "user/test")
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
    void throwsOnInvalidNumber() throws Exception {
        final int number = 0;
        final String repo = "user/test";
        final Response response = new JdkRequest(
            RetrieveTicketByNumberITCase.RAW.formatted(this.port)
        ).uri()
            .queryParam("number", number)
            .queryParam("repo", repo)
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
                        "404 NOT_FOUND \"Ticket with issue = %s and repo = %s not found\""
                            .formatted(number, repo)
                    ).toString()
            )
        );
    }

    @Test
    void throwsOnEmptyNumber() throws Exception {
        final String repo = "user/test";
        final Response response = new JdkRequest(
            RetrieveTicketByNumberITCase.RAW.formatted(this.port)
        ).uri()
            .queryParam("repo", repo)
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
            new IsEqual<>(400)
        );
        MatcherAssert.assertThat(
            "Message %s isn't correct".formatted(response.body()),
            response.body(),
            new IsEqual<>(
                new MutableJson()
                    .with(
                        "message",
                        "Either job or number must be present"
                    ).toString()
            )
        );
    }

}
