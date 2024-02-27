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

package it.platforms.github.webhook;

import git.tracehub.pmo.platforms.github.webhook.CreateWebhook;
import git.tracehub.pmo.platforms.github.webhook.ExistsWebhook;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link CreateWebhook}.
 *
 * @since 0.0.0
 */
final class CreateWebhookIT {

    @Test
    void createsWebhookSuccessfully() {
        final String host = "https://api.github.com";
        final String url = "http://it/webhook";
        final String location = "hizmailovich/draft";
        final String token = System.getProperty("GithubToken");
        new CreateWebhook(
            "https://api.github.com",
            token,
            location,
            url,
            new ListOf<>("push", "issues")
        ).exec();
        MatcherAssert.assertThat(
            "Webhook %s isn't created as expected".formatted(url),
            new ExistsWebhook(host, location, token, url).value(),
            new IsEqual<>(true)
        );
    }

}
