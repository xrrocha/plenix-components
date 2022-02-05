package plenix.components.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * SQLExecutorFactoryImpl.
 */
public class SQLExecutorFactoryImpl extends AbstractSQLExecutor implements SQLExecutorFactory {
    private int batchSize = 1;
    private boolean ignoreErrors = false;

    /**
     * @see plenix.components.jdbc.SQLExecutorFactory#newInstance(java.sql.Connection)
     */
    public SQLExecutor newInstance(final Connection c) throws SQLException {
        return new SQLExecutor() {
            private int count = 0;
            private Connection connection = c;
            private PreparedStatement statement = null;
            
            public void execute(Map parameters) throws SQLException {
                if (statement == null){
                    statement = prepare(c);
                }
                setParameters(statement, parameters);
                statement.addBatch();
                if (++count == batchSize) {
                    executeBatch();
                }
            }

            public void close() throws SQLException {
                if (statement != null) {
                    if (count > 0) {
                        executeBatch();
                    }
                    statement.close();
                }
            }
            
            private void executeBatch() throws SQLException {
                try {
                    count = 0;
                    statement.executeBatch();
                } catch (SQLException e) {
                    if (!ignoreErrors) {
                        throw e;
                    }
                }
            }
        };
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setIgnoreErrors(boolean ignoreErrors) {
        this.ignoreErrors = ignoreErrors;
    }

    public boolean getIgnoreErrors() {
        return ignoreErrors;
    }
}
