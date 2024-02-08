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
import git.tracehub.pmo.ticket.DefaultTickets;
import git.tracehub.pmo.ticket.Ticket;
import git.tracehub.pmo.ticket.Tickets;
import it.PostgresIntegration;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration tests for {@link DefaultTickets}.
 *
 * @since 0.0.0
 */
@ActiveProfiles("pgit")
@SpringBootTest(classes = PmoApplication.class)
@SuppressWarnings("JTCOP.RuleAllTestsHaveProductionClass")
final class DefaultTicketsIT implements PostgresIntegration {

    /**
     * Tickets.
     */
    @Autowired
    private Tickets tickets;

    @Test
    @Sql("classpath:pre/sql/projects.sql")
    void returnsTicketByJob() {
        final Ticket expected = new Ticket(
            UUID.fromString("04986038-6e38-4928-b12e-644c99f9cadc"),
            UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"),
            1,
            "user/test",
            "path/to/job",
            Ticket.Status.OPENED
        );
        final Ticket ticket = this.tickets.byJob(
            expected.getJob(),
            expected.getRepo()
        );
        MatcherAssert.assertThat(
            "Ticket %s is null".formatted(ticket),
            true,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Ticket %s isn't correct".formatted(ticket),
            ticket,
            new IsEqual<>(expected)
        );
    }

    @Test
    @Sql("classpath:pre/sql/projects.sql")
    void returnsTicketByIssueNumber() {
        final Ticket expected = new Ticket(
            UUID.fromString("04986038-6e38-4928-b12e-644c99f9cadc"),
            UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"),
            1,
            "user/test",
            "path/to/job",
            Ticket.Status.OPENED
        );
        final Ticket ticket = this.tickets.byNumber(
            expected.getNumber(),
            expected.getRepo()
        );
        MatcherAssert.assertThat(
            "Ticket %s is null".formatted(ticket),
            true,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Ticket %s isn't correct".formatted(ticket),
            ticket,
            new IsEqual<>(expected)
        );
    }

    @Test
    @Sql("classpath:pre/sql/projects.sql")
    void createsTicket() {
        final Ticket expected = new Ticket(
            UUID.fromString("04986038-6e38-4928-b12e-644c99f9cadc"),
            UUID.fromString("74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"),
            1,
            "user/test",
            "path/to/job",
            Ticket.Status.OPENED
        );
        final Ticket ticket = this.tickets.create(expected);
        MatcherAssert.assertThat(
            "Ticket %s is null".formatted(ticket),
            true,
            Matchers.notNullValue()
        );
    }

}
