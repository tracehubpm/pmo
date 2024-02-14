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

import org.cactoos.Scalar;

/**
 * Tickets.
 *
 * @since 0.0.0
 */
public interface Tickets {

    /**
     * Ticket by job and repository.
     *
     * @param job Job path
     * @param repo Repository name
     * @return Ticket
     */
    Ticket byJob(String job, String repo);

    /**
     * Ticket by number and repository.
     *
     * @param number Issue number
     * @param repo Repository name
     * @return Ticket
     */
    Ticket byNumber(int number, String repo);

    /**
     * Create ticket.
     *
     * @param ticket Ticket
     * @return Ticket
     */
    Ticket create(Scalar<Ticket> ticket);

}
