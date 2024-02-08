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

package git.tracehub.pmo.controller.request;

import git.tracehub.pmo.ticket.Ticket;
import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

/**
 * Ticket from request.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class TicketFromReq implements Scalar<Ticket> {

    /**
     * Request ticket.
     */
    private final RqTicket ticket;

    @Override
    public Ticket value() {
        return new Ticket(
            this.ticket.project(),
            this.ticket.number(),
            this.ticket.repo(),
            this.ticket.job(),
            this.ticket.status()
        );
    }

}
