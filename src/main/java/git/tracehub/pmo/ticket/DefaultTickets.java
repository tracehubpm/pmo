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

import com.jcabi.jdbc.JdbcSession;
import git.tracehub.pmo.exception.ResourceNotFoundException;
import git.tracehub.pmo.project.SqlStatement;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.Scalar;
import org.springframework.stereotype.Component;

/**
 * Default tickets.
 *
 * @checkstyle DesignForExtensionCheck (70 lines)
 * @since 0.0.0
 */
@Component
@RequiredArgsConstructor
public class DefaultTickets implements Tickets {

    /**
     * Datasource.
     */
    private final DataSource source;

    @Override
    @SneakyThrows
    public Ticket byJob(final String job, final String repo) {
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("select-ticket-by-job-and-repo.sql").asString()
            ).set(job)
            .set(repo)
            .select(
                (rs, stmt) -> {
                    if (!rs.next()) {
                        throw new ResourceNotFoundException(
                            "Ticket with job = %s and repo = %s not found"
                                .formatted(job, repo)
                        );
                    }
                    return new TicketOf(rs).value();
                }
            );
    }

    @Override
    @SneakyThrows
    public Ticket byNumber(final int number, final String repo) {
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("select-ticket-by-number-and-repo.sql").asString()
            ).set(number)
            .set(repo)
            .select(
                (rs, stmt) -> {
                    if (!rs.next()) {
                        throw new ResourceNotFoundException(
                            "Ticket with issue = %s and repo = %s not found"
                                .formatted(number, repo)
                        );
                    }
                    return new TicketOf(rs).value();
                }
            );
    }

    @Override
    @SneakyThrows
    public Ticket create(final Scalar<Ticket> ticket) {
        final Ticket value = ticket.value();
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("insert-ticket.sql").asString()
            ).set(value.getProject())
            .set(value.getNumber())
            .set(value.getRepo())
            .set(value.getJob())
            .set(value.getStatus().name())
            .update(
                (rs, stmt) -> {
                    rs.next();
                    return new TicketOf(rs).value();
                }
            );
    }

}
