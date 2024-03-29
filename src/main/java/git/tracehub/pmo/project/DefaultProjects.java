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

package git.tracehub.pmo.project;

import com.jcabi.jdbc.JdbcSession;
import git.tracehub.pmo.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.Scalar;
import org.springframework.stereotype.Component;

/**
 * Default implementation of projects.
 *
 * @checkstyle DesignForExtensionCheck (70 lines)
 * @since 0.0.0
 */
@Component
@RequiredArgsConstructor
public class DefaultProjects implements Projects {

    /**
     * Datasource.
     */
    private final DataSource source;

    @Override
    @SneakyThrows
    public Project byId(final UUID id) {
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("select-project-by-id.sql").asString()
            ).set(id)
            .select(
                (rs, stmt) -> {
                    if (!rs.next()) {
                        throw new ResourceNotFoundException(
                            "Project %s not found".formatted(id)
                        );
                    }
                    return new ProjectOf(rs).value();
                }
            );
    }

    @Override
    @SneakyThrows
    public List<Project> byUser(final String login) {
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("select-projects-by-user-login.sql")
                    .asString()
            ).set(login)
            .select(
                (rs, stmt) -> {
                    final List<Project> projects = new ArrayList<>(5);
                    while (rs.next()) {
                        projects.add(new ProjectOf(rs).value());
                    }
                    return projects;
                }
            );
    }

    @Override
    @SneakyThrows
    public Project employ(final Scalar<Project> project) {
        final Project value = project.value();
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("insert-project.sql").asString()
            ).set(value.getName())
            .set(value.getLocation())
            .set(value.getDescription())
            .set(value.isActive())
            .update(
                (rs, stmt) -> {
                    rs.next();
                    return new ProjectOf(rs).value();
                }
            );
    }
}
