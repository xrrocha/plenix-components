package plenix.components.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * SQLQueryExecutorImpl.
 */
public class SQLQueryExecutorImpl extends AbstractSQLExecutor implements SQLQueryExecutor {
    private int fetchSize;

    /**
     * @see plenix.components.jdbc.SQLQueryExecutor#executeQuery(java.sql.Connection, java.util.Map)
     */
    public ResultSet executeQuery(Connection connection, Map parameters) throws SQLException {
        PreparedStatement statement = prepare(connection);
        setParameters(statement, parameters);
        try {
            ResultSet resultSet = statement.executeQuery();
            resultSet.setFetchSize(fetchSize);
            return resultSet;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getFetchSize() {
        return fetchSize;
    }
}
