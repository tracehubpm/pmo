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

package git.tracehub.pmo.database;

import git.tracehub.pmo.project.Project;
import git.tracehub.pmo.ticket.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Jdbc test.
 *
 * @since 0.0.0
 */
public class JdbcTest {

    /**
     * Result set.
     *
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Mock
    protected ResultSet set;

    /**
     * Datasource.
     */
    @Mock
    private DataSource source;

    /**
     * Connection.
     */
    @Mock
    private Connection connection;

    /**
     * Statement.
     */
    @Mock
    private PreparedStatement statement;

    /**
     * Mock result set.
     *
     * @param ticket Ticket
     * @throws SQLException If something goes wrong
     */
    protected void mockResultSet(final Ticket ticket) throws SQLException {
        Mockito.when(this.set.next()).thenReturn(true);
        Mockito.when(this.set.getString("id"))
            .thenReturn(ticket.getId().toString());
        Mockito.when(this.set.getString("project"))
            .thenReturn(ticket.getProject().toString());
        Mockito.when(this.set.getInt("number"))
            .thenReturn(ticket.getNumber());
        Mockito.when(this.set.getString("repo"))
            .thenReturn(ticket.getRepo());
        Mockito.when(this.set.getString("job"))
            .thenReturn(ticket.getJob());
        Mockito.when(this.set.getString("status"))
            .thenReturn(ticket.getStatus().name());
    }

    /**
     * Mock result set.
     *
     * @param project Project
     * @throws SQLException If something goes wrong
     */
    protected void mockResultSet(final Project project) throws SQLException {
        Mockito.when(this.set.getString("id"))
            .thenReturn(project.getId().toString());
        Mockito.when(this.set.getString("name"))
            .thenReturn(project.getName());
        Mockito.when(this.set.getString("location"))
            .thenReturn(project.getLocation());
        Mockito.when(this.set.getString("description"))
            .thenReturn(project.getDescription());
        Mockito.when(this.set.getBoolean("active"))
            .thenReturn(project.isActive());
    }

    /**
     * Set datasource and connection.
     *
     * @throws SQLException If something goes wrong
     */
    @BeforeEach
    void setConnection() throws SQLException {
        Mockito.when(this.source.getConnection()).thenReturn(this.connection);
        Mockito.doNothing().when(this.connection).setAutoCommit(true);
        Mockito.lenient().when(this.connection.prepareStatement(Mockito.anyString()))
            .thenReturn(this.statement);
        Mockito.lenient()
            .when(this.connection.prepareStatement(Mockito.anyString(), Mockito.anyInt()))
            .thenReturn(this.statement);
        Mockito.doNothing().when(this.statement).close();
        Mockito.lenient().when(this.statement.executeQuery()).thenReturn(this.set);
        Mockito.lenient().when(this.statement.getGeneratedKeys()).thenReturn(this.set);
    }

}
