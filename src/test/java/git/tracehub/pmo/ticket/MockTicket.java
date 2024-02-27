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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.Proc;
import org.mockito.Mockito;

/**
 * Mock ticket into ResultSet.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
@SuppressWarnings({
    "JTCOP.RuleCorrectTestName",
    "JTCOP.RuleAllTestsHaveProductionClass"
})
final class MockTicket implements Proc<Ticket> {

    /**
     * ResultSet.
     */
    private final ResultSet set;

    @Override
    @SneakyThrows
    public void exec(final Ticket ticket) {
        Mockito.when(this.set.getString("id"))
            .thenReturn(ticket.getId().toString());
        Mockito.when(this.set.getString("project"))
            .thenReturn(ticket.getProject().toString());
        Mockito.when(this.set.getInt("number"))
            .thenReturn(ticket.getNumber());
        Mockito.when(this.set.getString("repo"))
            .thenReturn(ticket.getRepo());
        Mockito.when(this.set.getString("job"))
            .thenReturn(ticket.getJob());
        Mockito.when(this.set.getString("status"))
            .thenReturn(ticket.getStatus().name());
    }

}
