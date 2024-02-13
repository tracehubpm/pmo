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

import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Test suite for {@link IdProvider}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class IdProviderTest {

    /**
     * JWT.
     */
    @Mock
    private Jwt jwt;

    @Test
    void returnsGithubAsProvider() {
        final String provider = "github";
        Mockito.when(this.jwt.getClaimAsMap("realm_access")).thenReturn(
            new MapOf<>(
                new MapEntry<>("roles", new ListOf<>("user_github"))
            )
        );
        MatcherAssert.assertThat(
            "Provider %s doesn't exist".formatted(provider),
            new IdProvider(this.jwt).value(),
            new IsEqual<>(provider)
        );
    }

    @Test
    void returnsGitlabAsProvider() {
        final String provider = "gitlab";
        Mockito.when(this.jwt.getClaimAsMap("realm_access")).thenReturn(
            new MapOf<>(
                new MapEntry<>("roles", new ListOf<>("user_gitlab"))
            )
        );
        MatcherAssert.assertThat(
            "Provider %s doesn't exist".formatted(provider),
            new IdProvider(this.jwt).value(),
            new IsEqual<>(provider)
        );
    }

}
