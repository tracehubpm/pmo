/*
 * Copyright (c) 2023-2024 Tracehub
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

package git.tracehub.pmo.agents.github;

import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import git.tracehub.pmo.agents.Action;
import io.github.eocqrs.eokson.Jocument;
import io.github.eocqrs.eokson.MutableJson;
import java.net.HttpURLConnection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
        new JdkRequest(
            "https://api.github.com/repos/%s/collaborators/%s".formatted(
                this.location.replaceAll(
                    "([^@/]+)@([^/]+)/([^:]+):.*", "$2/$3"
                ),
                this.username
            )
        ).method(Request.PUT)
            .body()
            .set(
                new Jocument(
                    new MutableJson()
                        .with("permission", "admin")
                ).toString()
            ).back()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                "Bearer %s".formatted(this.token)
            ).fetch()
            .as(RestResponse.class)
            .assertStatus(HttpURLConnection.HTTP_CREATED);
    }

}
