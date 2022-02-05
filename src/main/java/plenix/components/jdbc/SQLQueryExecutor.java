package plenix.components.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * SQLQueryExecutor.
 */
public interface SQLQueryExecutor {
    ResultSet executeQuery(Connection connection, Map parameters) throws SQLException;
}
