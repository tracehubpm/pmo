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

package git.tracehub.pmo.platforms.github.webhook;

import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link CreateWebhook}.
 *
 * @since 0.0.0
 */
final class CreateWebhookTest {

    @Test
    void createsWebhookSuccessfully() throws IOException {
        final MkContainer container = new MkGrizzlyContainer()
            .next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "[{\"config\":{\"content_type\":\"json\",\"url\":\"test/url\"},\"events\":[\"push\"]}]"
                )
            ).next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_CREATED
                )
            ).start();
        final String location = "user/repo";
        final String url = container.home().toString();
        new CreateWebhook(
            url.substring(0, url.length() - 1),
            "token",
            location,
            "url",
            new ListOf<>("push")
        ).exec();
        MatcherAssert.assertThat(
            "Webhook %s isn't created as expected".formatted(url),
            container.take().uri().toString(),
            new IsEqual<>("/repos/%s/hooks".formatted(location))
        );
        container.stop();
    }

    @Test
    void doesNotCreateWebhookWhenAlreadyExists() throws IOException {
        final MkContainer container = new MkGrizzlyContainer()
            .next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "[{\"config\":{\"content_type\":\"json\",\"url\":\"test/url\"},\"events\":[\"push\"]}]"
                )
            ).start();
        final String location = "user/repo";
        final String url = container.home().toString();
        new CreateWebhook(
            url.substring(0, url.length() - 1),
            "token",
            location,
            "test/url",
            new ListOf<>("push")
        ).exec();
        MatcherAssert.assertThat(
            "Webhook %s is created when it already exists".formatted(url),
            container.take().uri().toString(),
            new IsEqual<>("/repos/%s/hooks".formatted(location))
        );
        container.stop();
    }

    @Test
    void throwsOnInvalidHost() {
        final String url = "http://localhost:1000";
        final String location = "user/repo";
        Assertions.assertThrows(
            IOException.class,
            () -> new CreateWebhook(
                url,
                "token",
                location,
                "test/url",
                new ListOf<>("push")
            ).exec(),
            "Exception is not thrown or valid"
        );
    }

}
