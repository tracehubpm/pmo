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

package git.tracehub.pmo.extensions;

import org.cactoos.list.ListOf;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Mocked MethodArgumentNotValidException.
 *
 * @since 0.0.0
 */
@SuppressWarnings(
    {"JTCOP.RuleCorrectTestName", "JTCOP.RuleAllTestsHaveProductionClass"}
)
public class MkArgumentException implements ParameterResolver {

    @Override
    public boolean supportsParameter(
        final ParameterContext parameterContext,
        final ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        return MethodArgumentNotValidException.class
            .isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(
        final ParameterContext parameterContext,
        final ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        final MethodArgumentNotValidException exception =
            Mockito.mock(MethodArgumentNotValidException.class);
        final BindingResult result = Mockito.mock(BindingResult.class);
        Mockito.when(exception.getBindingResult()).thenReturn(result);
        Mockito.when(result.getFieldErrors()).thenReturn(
            new ListOf<>(
                new FieldError(
                    "object",
                    "field",
                    "Message")
            )
        );
        return exception;
    }

}
