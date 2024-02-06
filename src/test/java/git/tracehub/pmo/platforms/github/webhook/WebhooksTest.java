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

import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.Throws;

/**
 * Test suite for {@link Webhooks}.
 *
 * @since 0.0.0
 */
final class WebhooksTest {

    @Test
    void returnsListOfWebhooks() throws IOException {
        final Webhook webhook = new Webhook(
            new MapOf<String, String>(
                new MapEntry<>("url", "test/url"),
                new MapEntry<>("content_type", "json")
            ),
            new ListOf<>("push")
        );
        final MkContainer container = new MkGrizzlyContainer()
            .next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "[%s]".formatted(webhook.asString())
                )
            ).start();
        final String url = container.home().toString();
        final List<Webhook> webhooks =
            new Webhooks(
                url.substring(0, url.length() - 1),
                "user/repo",
                "token"
            ).value();
        MatcherAssert.assertThat(
            "List of webhooks is equal to null",
            webhooks,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "List of webhooks doesn't contain %s".formatted(webhook),
            webhooks,
            Matchers.contains(webhook)
        );
        container.stop();
    }

    @Test
    @SuppressWarnings("JTCOP.RuleAssertionMessage")
    void throwsOnInvalidHost() {
        new Assertion<>(
            "Exception is not thrown or valid",
            () ->  new Webhooks(
                "http://localhost:1000",
                "user/repo",
                "token"
            ).value(),
            new Throws<>(IOException.class)
        ).affirm();
    }

}
