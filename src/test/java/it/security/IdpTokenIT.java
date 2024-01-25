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

package it.security;

import com.jcabi.http.mock.MkAnswer;
import git.tracehub.pmo.PmoApplication;
import git.tracehub.pmo.security.IdpToken;
import it.PostgresIntegration;
import it.ServerIntegration;
import java.net.HttpURLConnection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for {@link IdpToken}.
 *
 * @since 0.0.0
 */
@ActiveProfiles("web")
@SpringBootTest(
    classes = PmoApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@SuppressWarnings("JTCOP.RuleAllTestsHaveProductionClass")
final class IdpTokenIT
    implements ServerIntegration, PostgresIntegration {

    @Test
    void retrievesIdpTokenSuccessfully() {
        final String url = ServerIntegration.SERVER.home().toString();
        final String expected = "token";
        ServerIntegration.SERVER.next(
            new MkAnswer.Simple(
                HttpURLConnection.HTTP_OK,
                "access_token=%s&expires_in=3600&token_type=bearer"
                    .formatted(expected)
            )
        );
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
    }

}
