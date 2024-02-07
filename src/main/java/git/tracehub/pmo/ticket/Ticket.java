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

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ticket.
 *
 * @since 0.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    /**
     * Id.
     */
    private UUID id;

    /**
     * Project id.
     */
    private UUID project;

    /**
     * Issue number.
     */
    private int number;

    /**
     * Repository.
     */
    private String repo;

    /**
     * Job.
     */
    private String job;

    /**
     * Status.
     */
    private Status status;

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

}
