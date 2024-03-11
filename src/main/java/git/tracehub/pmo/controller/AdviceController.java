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

import git.tracehub.pmo.exception.Logged;
import git.tracehub.pmo.exception.RestError;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception handler.
 *
 * @checkstyle NonStaticMethodCheck (120 lines)
 * @since 0.0.0
 */
@RestControllerAdvice
public class AdviceController {

    /**
     * Handle MethodArgumentNotValidException.
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<byte[]> handle(
        final MethodArgumentNotValidException exception
    ) {
        return new Logged(
            exception,
            new RestError(
                exception.getBindingResult().getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", ")),
                HttpStatus.BAD_REQUEST
            )
        ).value();
    }

    /**
     * Handle ResponseStatusException.
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<byte[]> handle(
        final ResponseStatusException exception
    ) {
        return new Logged(
            exception,
            new RestError(
                exception.getMessage(),
                HttpStatus.valueOf(exception.getStatusCode().value())
            )
        ).value();
    }

    /**
     * Handle IllegalArgumentException.
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<byte[]> handle(
        final IllegalArgumentException exception
    ) {
        return new Logged(
            exception,
            new RestError(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
            )
        ).value();
    }

    /**
     * Handle AccessDeniedException.
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<byte[]> handle(
        final AccessDeniedException exception
    ) {
        return new Logged(
            exception,
            new RestError(
                exception.getMessage(),
                HttpStatus.FORBIDDEN
            )
        ).value();
    }

    /**
     * Handle Exception.
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<byte[]> handle(final Exception exception) {
        return new Logged(
            exception,
            new RestError(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        ).value();
    }

}
