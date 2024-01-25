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

import com.jcabi.github.Collaborators;
import com.jcabi.github.Github;
import com.jcabi.github.Repo;
import com.jcabi.github.Repos;
import com.jcabi.github.mock.MkGithub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test suite for {@link InviteCollaborator}.
 *
 * @since 0.0.0
 */
final class InviteCollaboratorTest {

    @Test
    void invitesCollaboratorSuccessfully() {
        final Github github = Mockito.mock(Github.class);
        final Repos repos = Mockito.mock(Repos.class);
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(github.repos()).thenReturn(repos);
        Mockito.when(repos.get(Mockito.any())).thenReturn(repo);
        Mockito.when(repo.collaborators()).thenReturn(Mockito.mock(Collaborators.class));
        new InviteCollaborator("user/repo", "user", github).exec();
        Mockito.verify(github, Mockito.times(1)).repos();
        Mockito.verify(repos, Mockito.times(1)).get(Mockito.any());
        Mockito.verify(repo, Mockito.times(1)).collaborators();
    }

    @Test
    void trowsOnInvalidLocation() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new InviteCollaborator("user", "user", new MkGithub("user"))
                .exec(),
            "Exception is not thrown or valid"
        );
    }

}
