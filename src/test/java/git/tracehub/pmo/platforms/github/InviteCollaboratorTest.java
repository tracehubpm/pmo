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

import com.jcabi.github.Repo;
import com.jcabi.github.mock.MkGithub;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link InviteCollaborator}.
 *
 * @since 0.0.0
 */
final class InviteCollaboratorTest {

    @Test
    void invitesCollaboratorSuccessfully() throws IOException {
        final String collaborator = "name";
        final Repo repo = new MkGithub("user").randomRepo();
        new InviteCollaborator(repo, collaborator).exec();
        MatcherAssert.assertThat(
            "Collaborator %s isn't invited as expected"
                .formatted(collaborator),
            repo.collaborators().isCollaborator(collaborator),
            new IsEqual<>(true)
        );
    }

}
