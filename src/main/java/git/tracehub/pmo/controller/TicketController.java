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

package git.tracehub.pmo.controller;

import git.tracehub.pmo.controller.request.RqTicket;
import git.tracehub.pmo.controller.request.TicketFromReq;
import git.tracehub.pmo.ticket.Ticket;
import git.tracehub.pmo.ticket.Tickets;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ticket Controller.
 *
 * @since 0.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    /**
     * Tickets.
     */
    private final Tickets tickets;

    /**
     * Ticket by path to job and repository.
     *
     * @param repo Repository name
     * @param job Path to job
     * @param number Issue number
     * @return Ticket
     */
    @GetMapping
    @Operation(summary = "Get ticket by job path or issue number")
    public Ticket byJob(
        @RequestParam final String repo,
        @RequestParam final Optional<String> job,
        @RequestParam final Optional<Integer> number
    ) {
        final Ticket ticket;
        if (job.isPresent()) {
            ticket = this.tickets.byJob(job.get(), repo);
        } else if (number.isPresent()) {
            ticket = this.tickets.byNumber(number.get(), repo);
        } else {
            throw new IllegalArgumentException(
                "Either job or number must be present"
            );
        }
        return ticket;
    }

    /**
     * Create ticket.
     *
     * @param ticket Ticket
     * @return Ticket
     */
    @PostMapping
    @Operation(summary = "Create a new ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket create(@RequestBody @Valid final RqTicket ticket) {
        return this.tickets.create(new TicketFromReq(ticket));
    }

}
