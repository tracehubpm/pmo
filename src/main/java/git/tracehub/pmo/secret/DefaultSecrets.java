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

package git.tracehub.pmo.secret;

import com.jcabi.jdbc.JdbcSession;
import git.tracehub.pmo.exception.ResourceNotFoundException;
import git.tracehub.pmo.project.SqlStatement;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.Scalar;
import org.springframework.stereotype.Component;

/**
 * Default secrets.
 *
 * @checkstyle DesignForExtensionCheck (100 lines)
 * @since 0.0.0
 */
@Component
@RequiredArgsConstructor
public class DefaultSecrets implements Secrets {

    /**
     * Datasource.
     */
    private final DataSource source;

    @Override
    @SneakyThrows
    public Secret value(final Key key) {
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("select-secret-by-key.sql").asString()
            ).set(key.getProject())
            .set(key.getKey())
            .select(
                (rs, stmt) -> {
                    if (!rs.next()) {
                        throw new ResourceNotFoundException(
                            "Secret with project = %s and key = %s not found"
                                .formatted(key.getProject(), key.getKey())
                        );
                    }
                    return new SecretOf(rs).value();
                }
            );
    }

    @Override
    @SneakyThrows
    public Secret create(final Scalar<Secret> secret) {
        final Secret value = secret.value();
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("insert-secret.sql").asString()
            ).set(value.getProject())
            .set(value.getKey())
            .set(value.getValue())
            .update(
                (rs, stmt) -> {
                    rs.next();
                    return new SecretOf(rs).value();
                }
            );
    }

    @Override
    @SneakyThrows
    public Secret update(final Scalar<Secret> secret) {
        final Secret value = secret.value();
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("update-secret.sql").asString()
            ).set(value.getValue())
            .set(value.getProject())
            .set(value.getKey())
            .update(
                (rs, stmt) -> {
                    rs.next();
                    return new SecretOf(rs).value();
                }
            );
    }

}
