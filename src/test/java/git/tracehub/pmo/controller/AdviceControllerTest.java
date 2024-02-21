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

package git.tracehub.pmo.controller;

import git.tracehub.pmo.exception.ResourceAlreadyExistsException;
import git.tracehub.pmo.exception.ResourceNotFoundException;
import io.github.eocqrs.eokson.Jocument;
import io.github.eocqrs.eokson.MutableJson;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Test suite for {@link AdviceController}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class AdviceControllerTest {

    /**
     * Controller.
     */
    private final AdviceController controller = new AdviceController();

    @Test
    void handlesMethodArgumentNotValidException() {
        final String message = "Message";
        final MethodArgumentNotValidException exception =
            Mockito.mock(MethodArgumentNotValidException.class);
        final BindingResult result = Mockito.mock(BindingResult.class);
        Mockito.when(exception.getBindingResult()).thenReturn(result);
        Mockito.when(result.getFieldErrors()).thenReturn(
            new ListOf<>(new FieldError("object", "field", message))
        );
        MatcherAssert.assertThat(
            "MethodArgumentNotValidException isn't handled",
            this.controller.handle(exception),
            new IsEqual<>(
                new ResponseEntity<>(
                    new Jocument(
                        new MutableJson()
                            .with("message", message)
                    ).byteArray(),
                    HttpStatus.BAD_REQUEST
                )
            )
        );
    }

    @Test
    void handlesResourceAlreadyExistsException() {
        final String message = "Message";
        final ResourceAlreadyExistsException exception =
            new ResourceAlreadyExistsException(message);
        MatcherAssert.assertThat(
            "ResourceAlreadyExistsException isn't handled",
            this.controller.handle(exception),
            new IsEqual<>(
                new ResponseEntity<>(
                    new Jocument(
                        new MutableJson()
                            .with("message", message)
                    ).byteArray(),
                    HttpStatus.BAD_REQUEST
                )
            )
        );
    }

    @Test
    void handlesResourceNotFoundException() {
        final String message = "Message";
        final ResourceNotFoundException exception =
            new ResourceNotFoundException(message);
        MatcherAssert.assertThat(
            "ResourceNotFoundException isn't handled",
            this.controller.handle(exception),
            new IsEqual<>(
                new ResponseEntity<>(
                    new Jocument(
                        new MutableJson()
                            .with("message", message)
                    ).byteArray(),
                    HttpStatus.NOT_FOUND
                )
            )
        );
    }

    @Test
    void handlesAccessDeniedException() {
        final String message = "Message";
        final AccessDeniedException exception =
            new AccessDeniedException(message);
        MatcherAssert.assertThat(
            "AccessDeniedException isn't handled",
            this.controller.handle(exception),
            new IsEqual<>(
                new ResponseEntity<>(
                    new Jocument(
                        new MutableJson()
                            .with("message", message)
                    ).byteArray(),
                    HttpStatus.FORBIDDEN
                )
            )
        );
    }

    @Test
    void handlesException() {
        final String message = "Message";
        final Exception exception = new Exception(message);
        MatcherAssert.assertThat(
            "Exception isn't handled",
            this.controller.handle(exception),
            new IsEqual<>(
                new ResponseEntity<>(
                    new Jocument(
                        new MutableJson()
                            .with("message", message)
                    ).byteArray(),
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        );
    }




}
