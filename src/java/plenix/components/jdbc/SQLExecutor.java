package plenix.components.jdbc;

import java.sql.SQLException;
import java.util.Map;

/**
 * SQLExecutor.
 */
public interface SQLExecutor {
    void execute(Map parameters) throws SQLException;
    void close() throws SQLException;
}
