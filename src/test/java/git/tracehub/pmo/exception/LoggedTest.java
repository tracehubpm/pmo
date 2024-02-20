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

import java.io.IOException;
import org.cactoos.Scalar;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test suite for {@link Logged}.
 *
 * @since 0.0.0
 */
@ExtendWith(OutputCaptureExtension.class)
final class LoggedTest {

    @Test
    void logsException(final CapturedOutput output) throws Exception {
        final String message = "Message";
        final Scalar<ResponseEntity<byte[]>> handled = new RestError(
            message,
            HttpStatus.NOT_FOUND
        );
        MatcherAssert.assertThat(
            "Exception is not handled",
            new Logged(
                new ResourceNotFoundException(message),
                handled
            ).value(),
            new IsEqual<>(handled.value())
        );
        MatcherAssert.assertThat(
            "Exception is not logged",
            output.getOut(),
            new StringContains(true, message)
        );
    }

    @Test
    void throwsOnInvalidError() {
        new Assertion<>(
            "Exception is not thrown or valid",
            () -> new Logged(
                new ResourceAlreadyExistsException("Message"),
                () -> {
                    throw new IOException();
                }
            ).value(),
            new Throws<>(IOException.class)
        ).affirm();
    }

}
