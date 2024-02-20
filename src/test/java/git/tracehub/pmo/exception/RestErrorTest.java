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

package git.tracehub.pmo.exception;

import io.github.eocqrs.eokson.MutableJson;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test suite for {@link RestError}.
 *
 * @since 0.0.0
 */
final class RestErrorTest {

    @Test
    void preparesResponse() {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        final String message = "Message";
        final ResponseEntity<byte[]> expected = new ResponseEntity<>(
            new MutableJson()
                .with("message", message).toString().getBytes(),
            status
        );
        final ResponseEntity<byte[]> response = new RestError(
            message,
            status
        ).value();
        MatcherAssert.assertThat(
            "Response %s is not prepared".formatted(response),
            response,
            new IsEqual<>(expected)
        );
    }

}
