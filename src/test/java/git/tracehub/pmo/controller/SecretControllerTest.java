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

import git.tracehub.pmo.secret.Keys;
import git.tracehub.pmo.secret.Secrets;
import io.github.eocqrs.eokson.Jocument;
import io.github.eocqrs.eokson.JsonOf;
import io.github.eocqrs.eokson.MutableJson;
import java.util.UUID;
import org.cactoos.io.ResourceOf;
import org.jasypt.util.text.TextEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
 * Test suite for {@link SecretController}.
 *
 * @since 0.0.0
 */
@ActiveProfiles("web")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SecretController.class)
final class SecretControllerTest {

    /**
     * Base url.
     */
    private static final String URL = "/secrets";

    /**
     * Mocked mvc.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * Secrets.
     */
    @MockBean(name = "encryptedSecrets")
    @SuppressWarnings("PMD.UnusedPrivateField")
    private Secrets secrets;

    /**
     * Keys.
     */
    @MockBean
    @SuppressWarnings("PMD.UnusedPrivateField")
    private Keys keys;

    /**
     * Encryptor.
     */
    @MockBean
    @SuppressWarnings("PMD.UnusedPrivateField")
    private TextEncryptor encryptor;

    @Test
    void returnsForbiddenOnUnauthorizedUser() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.post(SecretControllerTest.URL)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void returnsSecretByKey() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.get(SecretControllerTest.URL)
                .param("project", UUID.randomUUID().toString())
                .param("key", "key")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void returnsKeysByProject() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.get("%s/keys".formatted(SecretControllerTest.URL))
                .param("project", UUID.randomUUID().toString())
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createsNewSecret() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.post(SecretControllerTest.URL)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    new Jocument(
                        new JsonOf(
                            new ResourceOf("data/secret.json").stream()
                        )
                    ).toString()
                )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void updatesSecret() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.put(SecretControllerTest.URL)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    new Jocument(
                        new JsonOf(
                            new ResourceOf("data/secret.json").stream()
                        )
                    ).toString()
                )
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void throwsOnInvalidRequestBody() throws Exception {
        this.mvc.perform(
            MockMvcRequestBuilders.post(SecretControllerTest.URL)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    new Jocument(
                        new JsonOf(
                            new ResourceOf("data/no-key-secret.json").stream()
                        )
                    ).toString()
                )
        ).andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(
                MockMvcResultMatchers.content().string(
                    new MutableJson()
                        .with("message", "Key can't be blank")
                        .toString()
            )
        );
    }

}
