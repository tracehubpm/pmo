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

import git.tracehub.pmo.exception.ResourceAlreadyExistsException;
import git.tracehub.pmo.exception.ResourceNotFoundException;
import git.tracehub.pmo.exception.RestError;
import git.tracehub.pmo.extensions.MkArgumentException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Test suite for {@link AdviceController}.
 *
 * @since 0.0.0
 */
final class AdviceControllerTest {

    @Test
    @ExtendWith(MkArgumentException.class)
    void handlesMethodArgumentNotValidException(
        final MethodArgumentNotValidException exception
    ) {
        MatcherAssert.assertThat(
            "MethodArgumentNotValidException isn't handled",
            new AdviceController().handle(exception),
            new IsEqual<>(
                new RestError("Message", HttpStatus.BAD_REQUEST).value()
            )
        );
    }

    @Test
    void handlesResourceAlreadyExistsException() {
        final String message = "ResourceAlreadyExistsException";
        MatcherAssert.assertThat(
            "ResourceAlreadyExistsException isn't handled",
            new AdviceController().handle(
                new ResourceAlreadyExistsException(message)
            ),
            new IsEqual<>(
                new RestError(message, HttpStatus.BAD_REQUEST).value()
            )
        );
    }

    @Test
    void handlesIllegalArgumentException() {
        final String message = "IllegalArgumentException";
        MatcherAssert.assertThat(
            "IllegalArgumentException isn't handled",
            new AdviceController().handle(
                new IllegalArgumentException(message)
            ),
            new IsEqual<>(
                new RestError(message, HttpStatus.BAD_REQUEST).value()
            )
        );
    }

    @Test
    void handlesResourceNotFoundException() {
        final String message = "ResourceNotFoundException";
        MatcherAssert.assertThat(
            "ResourceNotFoundException isn't handled",
            new AdviceController().handle(
                new ResourceNotFoundException(message)
            ),
            new IsEqual<>(
                new RestError(message, HttpStatus.NOT_FOUND).value()
            )
        );
    }

    @Test
    void handlesAccessDeniedException() {
        final String message = "AccessDeniedException";
        MatcherAssert.assertThat(
            "AccessDeniedException isn't handled",
            new AdviceController().handle(
                new AccessDeniedException(message)
            ),
            new IsEqual<>(
                new RestError(message, HttpStatus.FORBIDDEN).value()
            )
        );
    }

    @Test
    void handlesException() {
        final String message = "Exception";
        MatcherAssert.assertThat(
            "Exception isn't handled",
            new AdviceController().handle(
                new Exception(message)
            ),
            new IsEqual<>(
                new RestError(message, HttpStatus.INTERNAL_SERVER_ERROR).value()
            )
        );
    }

}
