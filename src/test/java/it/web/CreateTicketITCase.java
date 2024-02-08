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
import git.tracehub.pmo.controller.TicketController;
import io.github.eocqrs.eokson.Jocument;
import io.github.eocqrs.eokson.JsonOf;
import io.github.eocqrs.eokson.MutableJson;
import it.KeycloakIntegration;
import it.PostgresIntegration;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration Test Case for Creating Ticket
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
final class CreateTicketITCase
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
    void createsTicketSuccessfully() throws Exception {
        final Response response = new JdkRequest(
            CreateTicketITCase.RAW.formatted(this.port)
        ).method(Request.POST)
            .body()
            .set(
                new Jocument(
                    new JsonOf(
                        new ResourceOf("data/ticket.json").stream()
                    )
                ).toString()
            ).back()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
            new IsEqual<>(201)
        );
    }

    @Test
    void throwsOnInvalidRequestBody() throws Exception {
        final Response response = new JdkRequest(
            CreateTicketITCase.RAW.formatted(this.port)
        ).method(Request.POST)
            .body()
            .set(
                new Jocument(
                    new JsonOf(
                        new ResourceOf("data/no-issue-ticket.json").stream()
                    )
                ).toString()
            ).back()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
                    .with("message", "Issue number can't be null")
                    .toString()
            )
        );
    }

}
