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

package git.tracehub.pmo.controller.request;

import git.tracehub.pmo.secret.Secret;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link SecretFromReq}.
 *
 * @since 0.0.0
 */
final class SecretFromReqTest {

    @Test
    void createsSecretFromRequest() {
        final Secret expected = new Secret(
            UUID.randomUUID(),
            "key",
            "value"
        );
        final Secret secret = new SecretFromReq(
            new RqSecret(
                expected.getProject(),
                expected.getKey(),
                expected.getValue()
            )
        ).value();
        MatcherAssert.assertThat(
            "Secret %s is null".formatted(secret),
            true,
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Secret %s isn't correct".formatted(secret),
            secret,
            Matchers.samePropertyValuesAs(expected, "id")
        );
    }

}
