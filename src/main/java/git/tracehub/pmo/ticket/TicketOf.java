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

import java.sql.ResultSet;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.Scalar;

/**
 * Ticket from result set.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class TicketOf implements Scalar<Ticket> {

    /**
     * Result set.
     */
    private final ResultSet set;

    @Override
    @SneakyThrows
    public Ticket value() {
        return new Ticket(
            UUID.fromString(this.set.getString("id")),
            UUID.fromString(this.set.getString("project")),
            this.set.getInt("number"),
            this.set.getString("repo"),
            this.set.getString("job"),
            Ticket.Status.valueOf(this.set.getString("status"))
        );
    }

}
