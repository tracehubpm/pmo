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

package git.tracehub.pmo.controller;

import git.tracehub.pmo.controller.request.RqSecret;
import git.tracehub.pmo.controller.request.SecretFromReq;
import git.tracehub.pmo.secret.Secret;
import git.tracehub.pmo.secret.Secrets;
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
     * Constructor.
     *
     * @param secrets Secrets
     */
    public SecretController(@Qualifier("validatedSecrets") final Secrets secrets) {
        this.secrets = secrets;
    }

    /**
     * Keys by project.
     *
     * @param project Project id
     * @return Secret
     */
    @GetMapping("/keys")
    public List<Secret> keys(@RequestParam final UUID project) {
        return this.secrets.keys(project);
    }

    /**
     * Secret value by key.
     *
     * @param project Project id
     * @param key Key
     * @return Secret
     */
    @GetMapping
    public Secret secret(
        @RequestParam final UUID project,
        @RequestParam final String key
    ) {
        return this.secrets.value(project, key);
    }

    /**
     * Update secret.
     *
     * @param secret Secret
     * @return Secret
     */
    @PutMapping
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
    @ResponseStatus(HttpStatus.CREATED)
    public Secret create(@RequestBody @Valid final RqSecret secret) {
        return this.secrets.create(new SecretFromReq(secret));
    }

}
