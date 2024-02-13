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
 * @checkstyle DesignForExtensionCheck (70 lines)
 * @since 0.0.0
 */
@Component("github")
@RequiredArgsConstructor
public class GithubPlatform implements Platform {

    /**
     * Github host.
     */
    @Value("${platforms.github}")
    private String host;

    @Override
    @SneakyThrows
    public void createWebhook(
        final Scalar<String> token,
        final Scalar<String> location
    ) {
        new CreateWebhook(
            this.host,
            token.value(),
            location.value(),
            "http://it/webhook",
            new ListOf<>("push", "issues")
        ).exec();
    }

    @Override
    @SneakyThrows
    public void createLabel(
        final Scalar<String> token,
        final Scalar<String> location
    ) {
        new CreateLabels(
            new RtGithub(token.value()).repos()
                .get(new Coordinates.Simple(location.value())),
            new ListOf<>(new Label("new", Color.PINK))
        ).exec();
    }

    @Override
    @SneakyThrows
    public void inviteCollaborator(
        final Scalar<String> token,
        final Scalar<String> location
    ) {
        new InviteCollaborator(
            new RtGithub(token.value()).repos()
                .get(new Coordinates.Simple(location.value())),
            "tracehubgit"
        ).exec();
    }
}
