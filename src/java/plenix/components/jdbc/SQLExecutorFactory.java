package plenix.components.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SQLExecutorFactory.
 */
public interface SQLExecutorFactory {
    SQLExecutor newInstance(Connection connection) throws SQLException;
}
