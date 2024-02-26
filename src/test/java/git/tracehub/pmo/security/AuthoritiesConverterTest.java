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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Test suite for {@link AuthoritiesConverter}.
 *
 * @since 0.0.0
 */
@ExtendWith(MockitoExtension.class)
final class AuthoritiesConverterTest {

    /**
     * JWT.
     */
    @Mock
    private Jwt jwt;

    @Test
    void convertsJwtSuccessfully() {
        Mockito.when(this.jwt.getClaimAsMap("realm_access")).thenReturn(
            new MapOf<>(
                new MapEntry<>("roles", new ListOf<>("role"))
            )
        );
        final JwtAuthenticationToken token = new AuthoritiesConverter().convert(this.jwt);
        MatcherAssert.assertThat(
            "List of authorities is empty",
            token.getAuthorities().isEmpty(),
            new IsEqual<>(false)
        );
        MatcherAssert.assertThat(
            "List of authorities %s doesn't include role "
                .formatted(token.getAuthorities()),
            token.getAuthorities().contains(
                new SimpleGrantedAuthority("role")
            ),
            new IsEqual<>(true)
        );
    }

    @Test
    void returnsEmptyListWhenMapIsEmpty() {
        final JwtAuthenticationToken token = new AuthoritiesConverter().convert(this.jwt);
        MatcherAssert.assertThat(
            "List of authorities %s isn't empty"
                .formatted(token.getAuthorities()),
            token.getAuthorities().isEmpty(),
            new IsEqual<>(true)
        );
    }

    @Test
    void returnsEmptyListWhenMapIsNull() {
        Mockito.when(this.jwt.getClaimAsMap("realm_access")).thenReturn(null);
        final JwtAuthenticationToken token = new AuthoritiesConverter().convert(this.jwt);
        MatcherAssert.assertThat(
            "List of authorities %s isn't empty"
                .formatted(token.getAuthorities()),
            token.getAuthorities().isEmpty(),
            new IsEqual<>(true)
        );
    }

}
