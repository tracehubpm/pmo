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

import git.tracehub.pmo.exception.ResourceNotFoundException;
import io.github.eocqrs.eokson.Jocument;
import io.github.eocqrs.eokson.MutableJson;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler.
 *
 * @checkstyle NonStaticMethodCheck (90 lines)
 * @since 0.0.0
 */
@Slf4j
@RestControllerAdvice
public class AdviceController {

    /**
     * Handle MethodArgumentNotValidException.
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<byte[]> handle(
        final MethodArgumentNotValidException exception
    ) {
        log.warn(exception.getMessage(), exception);
        return new ResponseEntity<>(
            new Jocument(
                new MutableJson()
                    .with(
                        "message",
                        exception.getBindingResult().getFieldErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining(", "))
                    )
            ).byteArray(),
            HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle ResourceNotFoundException.
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<byte[]> handle(
        final ResourceNotFoundException exception
    ) {
        log.warn(exception.getMessage(), exception);
        return new ResponseEntity<>(
            new Jocument(
                new MutableJson()
                    .with("message", exception.getMessage())
            ).byteArray(),
            HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handle AccessDeniedException.
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<byte[]> handle(
        final AccessDeniedException exception
    ) {
        log.warn(exception.getMessage(), exception);
        return new ResponseEntity<>(
            new Jocument(
                new MutableJson()
                    .with("message", exception.getMessage())
            ).byteArray(),
            HttpStatus.FORBIDDEN
        );
    }

    /**
     * Handle Exception.
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<byte[]> handle(final Exception exception) {
        log.warn(exception.getMessage(), exception);
        return new ResponseEntity<>(
            new Jocument(
                new MutableJson()
                    .with("message", exception.getMessage())
            ).byteArray(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
