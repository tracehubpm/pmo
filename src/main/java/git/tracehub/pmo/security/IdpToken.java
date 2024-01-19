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

import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.Scalar;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Token from IDP.
 *
 * @since 0.0.0
 * @checkstyle MethodBodyCommentsCheck (50 lines)
 */
@RequiredArgsConstructor
public final class IdpToken implements Scalar<String> {

    /**
     * Token.
     */
    private final JwtAuthenticationToken token;

    /**
     * Provider name.
     */
    private final String provider;

    /**
     * Base url.
     */
    private final String url;

    @Override
    @SneakyThrows
    public String value() {
        /*
         * @todo #1:45min/DEV fix 403 Forbidden error when trying to get
         *   token from IDP. It seems that the user hasn't enough permissions
         *   to get the token from IDP. We need to configure Keycloak to allow
         *   the user to read the token. See the following link for more info:
         *   https://www.keycloak.org/docs/latest/server_admin/#retrieving-external-idp-tokens
         */
        new JdkRequest(
            "%s//broker/%s/token".formatted(
                this.url,
                this.provider
            )
        ).method(Request.GET)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                "Bearer %s".formatted(this.token.getToken().getTokenValue())
            ).fetch()
            .as(RestResponse.class);
        return null;
    }

}
