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

package git.tracehub.pmo.security.expression;

import git.tracehub.pmo.project.Project;
import git.tracehub.pmo.project.Projects;
import git.tracehub.pmo.security.ClaimOf;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Has project.
 *
 * @since 0.0.0
 */
@Component
@RequiredArgsConstructor
public class HasProject implements Expression<UUID> {

    /**
     * Projects.
     */
    private final Projects projects;

    @Override
    public boolean validate(final UUID project) {
        final Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Stream.concat(
                this.projects.byUser(
                    new ClaimOf(jwt, "email").value()
                ).stream(),
                this.projects.byUser(
                    new ClaimOf(jwt, "preferred_username").value()
                ).stream()
            ).map(Project::getId)
            .anyMatch(id -> id.equals(project));
    }

}
