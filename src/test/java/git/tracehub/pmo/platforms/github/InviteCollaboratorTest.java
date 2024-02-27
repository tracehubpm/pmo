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

import com.jcabi.github.Collaborators;
import com.jcabi.github.Repo;
import com.jcabi.github.mock.MkGithub;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
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

    @Test
    void throwsOnInvalidRepository() throws IOException {
        final String collaborator = "name";
        final Repo repo = Mockito.mock(Repo.class);
        final Collaborators collab = Mockito.mock(Collaborators.class);
        Mockito.when(repo.collaborators()).thenReturn(collab);
        Mockito.doThrow(IOException.class).when(collab).add(collaborator);
        Assertions.assertThrows(
            IOException.class,
            () -> new InviteCollaborator(repo, collaborator).exec(),
            "Exception is not thrown or valid"
        );
    }

}
