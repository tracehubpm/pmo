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

package git.tracehub.pmo.ticket;

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
 * Test suite for {@link TicketOf}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class TicketOfTest {

    /**
     * Result set.
     */
    @Mock
    private ResultSet set;

    @Test
    void returnsTicketWithAllFields() throws SQLException {
        final Ticket expected = new Ticket(
            UUID.randomUUID(),
            UUID.randomUUID(),
            35,
            "user/repo",
            "path/to/job",
            Ticket.Status.OPENED
        );
        Mockito.when(this.set.getString("id"))
            .thenReturn(expected.getId().toString());
        Mockito.when(this.set.getString("project"))
            .thenReturn(expected.getProject().toString());
        Mockito.when(this.set.getInt("number"))
            .thenReturn(expected.getNumber());
        Mockito.when(this.set.getString("repo"))
            .thenReturn(expected.getRepo());
        Mockito.when(this.set.getString("job"))
            .thenReturn(expected.getJob());
        Mockito.when(this.set.getString("status"))
            .thenReturn(expected.getStatus().name());
        final Ticket ticket = new TicketOf(this.set).value();
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
    void throwsOnInvalidResultSet() throws SQLException {
        Mockito.when(this.set.getString("id"))
            .thenThrow(SQLException.class);
        new Assertion<>(
            "Exception is not thrown or valid",
            () ->  new TicketOf(this.set).value(),
            new Throws<>(SQLException.class)
        ).affirm();
    }

}
