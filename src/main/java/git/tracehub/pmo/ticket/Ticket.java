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

import java.util.UUID;
import lombok.Data;

/**
 * Ticket.
 *
 * @checkstyle ParameterNumberCheck (90 lines)
 * @since 0.0.0
 */
@Data
public class Ticket {

    /**
     * Id.
     */
    private UUID id;

    /**
     * Project id.
     */
    private final UUID project;

    /**
     * Issue number.
     */
    private final int number;

    /**
     * Repository.
     */
    private final String repo;

    /**
     * Job.
     */
    private final String job;

    /**
     * Status.
     */
    private final Status status;

    /**
     * Status of the ticket.
     */
    public enum Status {

        /**
         * Opened.
         */
        OPENED,

        /**
         * Closed.
         */
        CLOSED

    }

    /**
     * Constructor.
     *
     * @param id Id
     * @param project Project id
     * @param number Issue number
     * @param repo Repository
     * @param job Job
     * @param status Status
     */
    public Ticket(
        final UUID id,
        final UUID project,
        final int number,
        final String repo,
        final String job,
        final Status status
    ) {
        this.id = id;
        this.project = project;
        this.number = number;
        this.repo = repo;
        this.job = job;
        this.status = status;
    }

    /**
     * Constructor.
     *
     * @param project Project id
     * @param number Issue number
     * @param repo Repository
     * @param job Job
     * @param status Status
     */
    public Ticket(
        final UUID project,
        final int number,
        final String repo,
        final String job,
        final Status status
    ) {
        this(null, project, number, repo, job, status);
    }

}
