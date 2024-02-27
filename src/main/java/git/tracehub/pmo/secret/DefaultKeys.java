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

package git.tracehub.pmo.secret;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import git.tracehub.pmo.project.SqlStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * Default keys.
 *
 * @checkstyle DesignForExtensionCheck (50 lines)
 * @since 0.0.0
 */
@Component
@RequiredArgsConstructor
public class DefaultKeys implements Keys {

    /**
     * Datasource.
     */
    private final DataSource source;

    @Override
    @SneakyThrows
    public List<Key> byProject(final UUID project) {
        return new JdbcSession(this.source)
            .sql(
                new SqlStatement("select-keys-by-project.sql").asString()
            ).set(project)
            .select(
                (rs, stmt) -> {
                    final List<Key> keys = new ArrayList<>(5);
                    while (rs.next()) {
                        keys.add(
                            new KeyOf(rs).value()
                        );
                    }
                    return keys;
                }
            );
    }

    @Override
    @SneakyThrows
    public boolean exists(final Key key) {
        return new JdbcSession(this.source)
            .sql(new SqlStatement("exists-key.sql").asString())
            .set(key.getProject())
            .set(key.getName())
            .select(new SingleOutcome<>(Boolean.class));
    }

}
