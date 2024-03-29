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

import git.tracehub.pmo.controller.request.RqSecret;
import git.tracehub.pmo.controller.request.SecretFromReq;
import git.tracehub.pmo.secret.Key;
import git.tracehub.pmo.secret.Keys;
import git.tracehub.pmo.secret.Secret;
import git.tracehub.pmo.secret.Secrets;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Secret Controller.
 *
 * @since 0.0.0
 */
@RestController
@RequestMapping("/secrets")
public class SecretController {

    /**
     * Tickets.
     */
    private final Secrets secrets;

    /**
     * Keys.
     */
    private final Keys keys;

    /**
     * Constructor.
     *
     * @param secrets Secrets
     * @param keys Keys
     */
    public SecretController(
        @Qualifier("encryptedSecrets") final Secrets secrets,
        final Keys keys
    ) {
        this.secrets = secrets;
        this.keys = keys;
    }

    /**
     * Keys by project.
     *
     * @param project Project id
     * @return List of keys
     */
    @GetMapping("/keys")
    @Operation(summary = "Get keys by project")
    public List<Key> byProject(@RequestParam final UUID project) {
        return this.keys.byProject(project);
    }

    /**
     * Secret value by key.
     *
     * @param key Key
     * @return Secret
     */
    @GetMapping
    @Operation(summary = "Get value by key")
    public Secret byValue(final Key key) {
        return this.secrets.value(key);
    }

    /**
     * Update secret.
     *
     * @param secret Secret
     * @return Secret
     */
    @PutMapping
    @Operation(summary = "Update secret")
    public Secret update(@RequestBody @Valid final RqSecret secret) {
        return this.secrets.update(new SecretFromReq(secret));
    }

    /**
     * Create secret.
     *
     * @param secret Secret
     * @return Secret
     */
    @PostMapping
    @Operation(summary = "Create a new secret")
    @ResponseStatus(HttpStatus.CREATED)
    public Secret create(@RequestBody @Valid final RqSecret secret) {
        return this.secrets.create(new SecretFromReq(secret));
    }

}
