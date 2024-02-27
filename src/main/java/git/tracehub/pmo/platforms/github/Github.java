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

package git.tracehub.pmo.platforms.github;

import com.jcabi.github.Coordinates;
import com.jcabi.github.RtGithub;
import git.tracehub.pmo.platforms.Label;
import git.tracehub.pmo.platforms.Platform;
import git.tracehub.pmo.platforms.github.webhook.CreateWebhook;
import java.awt.Color;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Github platform.
 *
 * @since 0.0.0
 */
@Component
@RequiredArgsConstructor
public final class Github implements Platform {

    /**
     * Github host.
     */
    @Value("${platforms.github}")
    private String host;

    @Override
    @SneakyThrows
    public void prepare(
        final Scalar<String> token,
        final Scalar<String> location
    ) {
        final String tkn = token.value();
        final String loc = location.value();
        new InviteCollaborator(
            new RtGithub(tkn).repos()
                .get(new Coordinates.Simple(loc)),
            "tracehubgit"
        ).exec();
        new CreateLabels(
            new RtGithub(tkn).repos()
                .get(new Coordinates.Simple(loc)),
            new ListOf<>(new Label("new", Color.PINK))
        ).exec();
        new CreateWebhook(
            this.host,
            tkn,
            loc,
            "http://it/webhook",
            new ListOf<>("push", "issues")
        ).exec();
    }
}
