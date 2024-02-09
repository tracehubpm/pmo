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

package git.tracehub.pmo.ticket;

import git.tracehub.pmo.database.JdbcTest;
import git.tracehub.pmo.exception.ResourceNotFoundException;
import java.sql.SQLException;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test suite for {@link DefaultTickets}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class DefaultTicketsTest extends JdbcTest {

    /**
     * Default tickets.
     */
    @InjectMocks
    private DefaultTickets tickets;

    @Test
    void returnsTicketByJob() throws SQLException {
        final Ticket expected = new Ticket(
            UUID.randomUUID(),
            UUID.randomUUID(),
            35,
            "user/repo",
            "path/to/job",
            Ticket.Status.OPENED
        );
        super.mockResultSet(expected);
        Mockito.when(super.set.next()).thenReturn(true);
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
    void returnsTicketByIssueNumber() throws SQLException {
        final Ticket expected = new Ticket(
            UUID.randomUUID(),
            UUID.randomUUID(),
            35,
            "user/repo",
            "path/to/job",
            Ticket.Status.OPENED
        );
        super.mockResultSet(expected);
        Mockito.when(super.set.next()).thenReturn(true);
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
    void createsTicket() throws SQLException {
        final Ticket expected = new Ticket(
            UUID.randomUUID(),
            UUID.randomUUID(),
            35,
            "user/repo",
            "path/to/job",
            Ticket.Status.OPENED
        );
        super.mockResultSet(expected);
        Mockito.when(super.set.next()).thenReturn(true);
        final Ticket ticket = this.tickets.create(expected);
        MatcherAssert.assertThat(
            "Ticket %s isn't created".formatted(ticket),
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
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnInvalidJob() throws SQLException {
        final String job = "invalid/path/to/job";
        final String repo = "repo";
        Mockito.when(super.set.next()).thenReturn(false);
        new Assertion<>(
            "Exception is not thrown or valid",
            () -> this.tickets.byJob(job, repo),
            new Throws<>(
                "Ticket with job = %s and repo = %s not found".formatted(job, repo),
                ResourceNotFoundException.class
            )
        ).affirm();
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnInvalidIssueNumber() throws SQLException {
        final int number = 35;
        final String repo = "repo";
        Mockito.when(super.set.next()).thenReturn(false);
        new Assertion<>(
            "Exception is not thrown or valid",
            () -> this.tickets.byNumber(number, repo),
            new Throws<>(
                "Ticket with issue = %s and repo = %s not found".formatted(number, repo),
                ResourceNotFoundException.class
            )
        ).affirm();
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnCreatingInvalidTicket() throws SQLException {
        Mockito.when(super.set.next()).thenThrow(SQLException.class);
        new Assertion<>(
            "Exception is not thrown or valid",
            () -> this.tickets.create(
                new Ticket(
                    UUID.randomUUID(),
                    35,
                    "user/repo",
                    "invalid/path/to/job",
                    Ticket.Status.OPENED
                )
            ),
            new Throws<>(SQLException.class)
        ).affirm();
    }

}
