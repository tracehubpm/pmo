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

package git.tracehub.pmo.security;

import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Test suite for {@link IdpToken}.
 *
 * @since 0.0.0
 */
final class IpdTokenTest {

    @Test
    void retrievesTokenSuccessfully() throws IOException {
        final String expected = "token";
        final MkContainer container = new MkGrizzlyContainer()
            .next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "access_token=%s&expires_in=3600&token_type=bearer"
                        .formatted(expected)
                )
            ).start();
        final String url = container.home().toString();
        final String token = new IdpToken(
            Mockito.mock(Jwt.class),
            "provider",
            url.substring(0, url.length() - 1)
        ).value();
        MatcherAssert.assertThat(
            "Access token %s isn't correct".formatted(token),
            token,
            new IsEqual<>(expected)
        );
        container.stop();
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnInvalidHost() {
        final String url = "http://localhost:1000";
        final String provider = "provider";
        new Assertion<>(
            "Exception is not thrown or valid",
            () -> new IdpToken(
                Mockito.mock(Jwt.class),
                provider,
                url
            ).value(),
            new Throws<>(
                "Failed GET request to %s/broker/%s/token".formatted(url, provider),
                IOException.class
            )
        ).affirm();
    }

}
