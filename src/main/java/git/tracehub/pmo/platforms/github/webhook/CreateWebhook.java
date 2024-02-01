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

package git.tracehub.pmo.platforms.github.webhook;

import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import git.tracehub.pmo.platforms.Action;
import java.net.HttpURLConnection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.springframework.http.HttpHeaders;

/**
 * Create webhook.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class CreateWebhook implements Action {

    /**
     * Github host.
     */
    private final String host;

    /**
     * Github token.
     */
    private final String token;

    /**
     * Owner and repo.
     */
    private final String location;

    /**
     * Webhook url.
     */
    private final String url;

    @Override
    @SneakyThrows
    public void exec() {
        new JdkRequest(
            "%s/repos/%s/hooks".formatted(
                this.host,
                this.location
            )
        ).method(Request.POST)
            .body()
            .set(
                new Webhook(
                    new MapOf<String, String>(
                        new MapEntry<>("url", this.url),
                        new MapEntry<>("content_type", "json")
                    ),
                    new ListOf<>("push"),
                    0L
                ).asString()
            ).back()
            .header(
                HttpHeaders.AUTHORIZATION,
                "Bearer %s".formatted(this.token)
            ).fetch()
            .as(RestResponse.class)
            .assertStatus(HttpURLConnection.HTTP_CREATED);
    }
}
