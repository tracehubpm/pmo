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

import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

/**
 * Does the webhook already exists.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
@SuppressWarnings({
    "JTCOP.RuleCorrectTestName",
    "JTCOP.RuleAllTestsHaveProductionClass"
})
public final class ExistsWebhook implements Scalar<Boolean> {

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
    public Boolean value() {
        return new Webhooks(this.host, this.location, this.token)
            .value()
            .stream()
            .anyMatch(
                content -> this.url.equals(
                    JsonParser.parseString(content.asString())
                        .getAsJsonObject()
                        .get("config").getAsJsonObject()
                        .get("url").getAsString()
                )
            );
    }

}

