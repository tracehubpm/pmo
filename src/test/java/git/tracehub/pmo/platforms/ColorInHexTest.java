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

package git.tracehub.pmo.platforms;

import java.awt.Color;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ColorInHex}.
 *
 * @since 0.0.0
 */
final class ColorInHexTest {

    @Test
    void returnsCorrectHexValue() {
        final String red = "ff0000";
        MatcherAssert.assertThat(
            "Hex value for red isn't equal to %s".formatted(red),
            new ColorInHex(Color.RED).value(),
            new IsEqual<>(red)
        );
        final String pink = "ffafaf";
        MatcherAssert.assertThat(
            "Hex value for pink isn't equal to %s".formatted(pink),
            new ColorInHex(Color.PINK).value(),
            new IsEqual<>(pink)
        );
        final String blue = "0000ff";
        MatcherAssert.assertThat(
            "Hex value for blue isn't equal to %s".formatted(blue),
            new ColorInHex(Color.BLUE).value(),
            new IsEqual<>(blue)
        );
    }

}
