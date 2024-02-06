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

import com.jcabi.github.Coordinates;
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;
import git.tracehub.pmo.platforms.Label;
import git.tracehub.pmo.platforms.RepoPath;
import git.tracehub.pmo.platforms.github.CreateLabels;
import git.tracehub.pmo.platforms.github.InviteCollaborator;
import git.tracehub.pmo.platforms.github.webhook.CreateWebhook;
import git.tracehub.pmo.project.Project;
import git.tracehub.pmo.project.Projects;
import git.tracehub.pmo.security.ClaimOf;
import git.tracehub.pmo.security.ExistsRole;
import git.tracehub.pmo.security.IdpToken;
import java.awt.Color;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.cactoos.list.ListOf;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project Controller.
 *
 * @since 0.0.0
 */
@RestController
@RequiredArgsConstructor
public class ProjectController {

    /**
     * Projects.
     */
    private final Projects projects;

    /**
     * Issuer url.
     */
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String url;

    /**
     * Github host.
     */
    @Value("${platforms.github}")
    private String host;

    /**
     * Projects by user.
     *
     * @param jwt Jwt
     * @return List of projects
     */
    @GetMapping
    public List<Project> byUser(@AuthenticationPrincipal final Jwt jwt) {
        return this.projects.byUser(
            new ClaimOf(jwt, "preferred_username").value()
        );
    }

    /**
     * Project by id.
     *
     * @param id Project id
     * @return Project
     */
    @GetMapping("/{id}")
    @PreAuthorize("@hasProject.validate(#id)")
    public Project byId(@PathVariable final UUID id) {
        return this.projects.byId(id);
    }

    /**
     * Employ new project.
     *
     * @param project Project
     * @param jwt Jwt
     * @return Project
     * @checkstyle MethodBodyCommentsCheck (20 lines)
     */
    @PostMapping()
    @PreAuthorize("hasAuthority('user_github')")
    @ResponseStatus(HttpStatus.CREATED)
    public Project employ(
        @RequestBody final Project project,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        final Project created = this.projects.employ(project);
        /*
         * @todo #1:45min/DEV define appropriate agent according to location
         *   of the project. We need to define appropriate agent and call
         *   corresponding implementation to invite collaborators here.
         */
        if (new ExistsRole(jwt, "user_github").value()) {
            final String location = new RepoPath(created.getLocation()).value();
            final String token = new IdpToken(jwt, "github", this.url).value();
            final Repo repo = new RtGithub(token).repos()
                .get(
                    new Coordinates.Simple(location)
                );
            new InviteCollaborator(
                repo,
                "tracehubgit"
            ).exec();
            new CreateLabels(
                repo,
                new ListOf<>(
                    new Label("new", Color.PINK)
                )
            ).exec();
            new CreateWebhook(
                this.host,
                token,
                location,
                "url",
                new ListOf<>("push")
            ).exec();
        }
        return created;
    }

}
