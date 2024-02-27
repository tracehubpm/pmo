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

package git.tracehub.pmo.controller;

import git.tracehub.pmo.platforms.Platform;
import git.tracehub.pmo.platforms.github.Github;
import git.tracehub.pmo.project.Project;
import git.tracehub.pmo.project.Projects;
import io.github.eocqrs.eokson.Jocument;
import io.github.eocqrs.eokson.JsonOf;
import io.github.eocqrs.eokson.MutableJson;
import java.util.Map;
import org.cactoos.Scalar;
import org.cactoos.io.ResourceOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test suite for {@link ProjectController}.
 *
 * @since 0.0.0
 */
@ActiveProfiles("web")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProjectController.class)
final class ProjectControllerTest {

    /**
     * Base url.
     */
    private static final String URL = "/projects";

    /**
     * Mocked mvc.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * Projects.
     */
    @MockBean
    @SuppressWarnings("PMD.UnusedPrivateField")
    private Projects projects;

    /**
     * Platforms.
     */
    @MockBean
    @SuppressWarnings("PMD.UnusedPrivateField")
    private Map<String, Platform> platforms;

    @Test
    void returnsForbiddenOnUnauthorizedUser() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.post(ProjectControllerTest.URL)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void returnsProjectByUser() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.get(ProjectControllerTest.URL)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void returnsProjectById() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.get(
                "%s/74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d"
                    .formatted(ProjectControllerTest.URL)
                ).with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createsNewProject() throws Exception {
        final Platform platform = Mockito.mock(Github.class);
        Mockito.when(this.platforms.get(Mockito.any()))
            .thenReturn(platform);
        Mockito.when(this.projects.employ(Mockito.any(Scalar.class)))
            .thenReturn(
                new Project(
                    "74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d",
                    "Location",
                    "Description",
                    true
                )
            );
        Mockito.doNothing().when(platform).prepare(Mockito.any(), Mockito.any());
        this.mvc.perform(
            MockMvcRequestBuilders.post(ProjectControllerTest.URL)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    new Jocument(
                        new JsonOf(
                            new ResourceOf("data/project.json").stream()
                        )
                    ).toString()
                )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void throwsOnInvalidRequestBody() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.post(ProjectControllerTest.URL)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    new Jocument(
                        new JsonOf(
                            new ResourceOf("data/no-name-project.json").stream()
                        )
                    ).toString()
                )
            ).andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(
                MockMvcResultMatchers.content().string(
                    new MutableJson()
                        .with("message", "Name can't be blank")
                        .toString()
            )
        );
    }

}
