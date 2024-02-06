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

import com.google.common.reflect.TypeToken;
import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import com.nimbusds.jose.shaded.gson.Gson;
import java.net.HttpURLConnection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.Scalar;
import org.springframework.http.HttpHeaders;

/**
 * Webhooks by repo.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class Webhooks implements Scalar<List<Webhook>> {

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

    @Override
    @SneakyThrows
    public List<Webhook> value() {
        return new Gson().fromJson(
            new JdkRequest(
                "%s/repos/%s/hooks".formatted(this.host, this.location)
            ).method(Request.GET)
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer %s".formatted(this.token)
                ).fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .body(),
            new TypeToken<List<Webhook>>() {
            }.getType()
        );
    }

}
