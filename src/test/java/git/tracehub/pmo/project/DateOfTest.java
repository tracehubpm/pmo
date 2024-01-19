/*
 * Copyright (c) 2023-2024 Tracehub
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

package git.tracehub.pmo.project;

import java.time.LocalDate;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link DateOf}.
 *
 * @since 0.0.0
 */
final class DateOfTest {

    @Test
    void returnsNullWhenStringIsEmpty() {
        final LocalDate date = new DateOf(null).value();
        MatcherAssert.assertThat(
            "Date %s is not empty".formatted(date),
            date,
            new IsEqual<>(null)
        );
    }

    @Test
    void returnsDateWhenStringExists() {
        final LocalDate date = new DateOf(
            LocalDate.of(2024, 1, 17).toString()
        ).value();
        MatcherAssert.assertThat(
            "Date %s is empty".formatted(date),
            date,
            new IsEqual<>(LocalDate.of(2024, 1, 17))
        );
    }

}
