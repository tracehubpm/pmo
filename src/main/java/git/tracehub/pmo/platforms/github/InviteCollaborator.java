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
import git.tracehub.pmo.platforms.Action;
import git.tracehub.pmo.platforms.RepoPath;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Invite collaborator.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class InviteCollaborator implements Action {

    /**
     * Owner and repo.
     */
    private final String location;

    /**
     * Username.
     */
    private final String username;

    /**
     * Token.
     */
    private final String token;

    @Override
    @SneakyThrows
    public void exec() {
        new RtGithub(this.token).repos()
            .get(
                new Coordinates.Simple(
                    new RepoPath(this.location).value()
                )
            ).collaborators()
            .add(this.username);
    }

}
