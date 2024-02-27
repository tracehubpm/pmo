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
import jakarta.validation.Valid;
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
     * @param job Path to job
     * @param repo Repository name
     * @return Ticket
     */
    @GetMapping("/job")
    public Ticket byJob(
        @RequestParam final String job,
        @RequestParam final String repo
    ) {
        return this.tickets.byJob(job, repo);
    }

    /**
     * Ticket by issue number and repository.
     *
     * @param number Issue number
     * @param repo Repository name
     * @return Ticket
     */
    @GetMapping("/number")
    public Ticket byNumber(
        @RequestParam final Integer number,
        @RequestParam final String repo
    ) {
        return this.tickets.byNumber(number, repo);
    }

    /**
     * Create ticket.
     *
     * @param ticket Ticket
     * @return Ticket
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket create(@RequestBody @Valid final RqTicket ticket) {
        return this.tickets.create(new TicketFromReq(ticket));
    }

}
