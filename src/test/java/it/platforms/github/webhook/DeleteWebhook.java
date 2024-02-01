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

package it.platforms.github.webhook;

import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import git.tracehub.pmo.platforms.Action;
import git.tracehub.pmo.platforms.github.webhook.Webhook;
import git.tracehub.pmo.platforms.github.webhook.Webhooks;
import java.net.HttpURLConnection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;

/**
 * Delete webhook if it already exists.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
@SuppressWarnings({
    "JTCOP.RuleCorrectTestName",
    "JTCOP.RuleAllTestsHaveProductionClass"
})
public final class DeleteWebhook implements Action {

    /**
     * Github host.
     */
    private final String host;

    /**
     * Location of repository.
     */
    private final String location;

    /**
     * Github token.
     */
    private final String token;

    /**
     * Webhook url.
     */
    private final String url;

    @Override
    @SneakyThrows
    public void exec() {
        final Optional<Long> id = new Webhooks(this.host, this.location, this.token)
            .value()
            .stream()
            .filter(
                content -> this.url.equals(
                    content.getConfig().getOrDefault("url", "")
                )
            ).map(Webhook::getId)
            .findFirst();
        if (id.isPresent()) {
            new JdkRequest(
                "https://api.github.com/repos/%s/hooks/%s"
                    .formatted(this.location, id.get())
            ).method(Request.DELETE)
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer %s".formatted(this.token)
                ).fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_NO_CONTENT);
        }
    }

}

