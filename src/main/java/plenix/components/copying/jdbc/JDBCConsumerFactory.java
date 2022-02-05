package plenix.components.copying.jdbc;

import plenix.components.copying.Consumer;
import plenix.components.copying.ConsumerFactory;
import plenix.components.copying.CopyingException;
import plenix.components.jdbc.SQLExecutor;
import plenix.components.jdbc.SQLExecutorFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBCConsumerFactory.
 */
public class JDBCConsumerFactory extends AbstractJDBCFactory implements ConsumerFactory {
    private SQLExecutorFactory[] before;
    private SQLExecutorFactory[] forEach;
    private SQLExecutorFactory[] after;

    /**
     * @see plenix.components.copying.ConsumerFactory#newInstance(java.lang.Object)
     */
    public Consumer newInstance(Object context) throws CopyingException {
        final Map parameters = (Map) context;
        return new Consumer() {
            private Connection connection = null;
            private SQLExecutor[] executors = null;

            public void consume(Object object) throws CopyingException {
                try {
                    if (connection == null) {
                        setup();
                    }
                    Map localParameters = null;
                    if (parameters == null) {
                        localParameters = (Map) object;
                    } else {
                        localParameters = new HashMap(parameters);
                        localParameters.putAll((Map) object);
                    }
                    for (int i = 0; i < executors.length; i++) {
                        executors[i].execute((Map) object);
                    }
                } catch (SQLException e) {
                    do {
                        debug(e.getMessage());
                        e = e.getNextException();
                    } while (e != null);
                    throw new CopyingException("Error during jdbc consuming: " + e, e);
                }
            }

            public void done() throws CopyingException {
                try {
                    if (connection == null) {
                        setup();
                    } else if (executors != null) {
                        for (int i = 0; i < executors.length; i++) {
                            executors[i].close();
                        }
                    }
                    execute(after, connection, parameters);
                } catch (Exception e) {
                    // debug("***" + ((SQLException) e).getNextException());
                    throw new CopyingException("Error closing jdbc consumer", e);
                } finally {
                    try {
                        connection.commit();
                    } catch (Exception s) {
                    }
                    try {
                        connection.close();
                    } catch (Exception s) {
                    }
                }
            }

            private void setup() throws SQLException {
                connection = getConnection();
                execute(before, connection, parameters);
                executors = new SQLExecutor[forEach.length];
                for (int i = 0; i < forEach.length; i++) {
                    executors[i] = forEach[i].newInstance(connection);
                }
            }
        };
    }

    private void execute(SQLExecutorFactory[] factory, Connection connection, Map parameters) throws SQLException {
        if (factory != null) {
            for (int i = 0; i < factory.length; i++) {
                SQLExecutor executor = factory[i].newInstance(connection);
                executor.execute(parameters);
                executor.close();
            }
        }
    }

    public void setForEach(SQLExecutorFactory[] sql) {
        this.forEach = sql;
    }

    public SQLExecutorFactory[] getForEach() {
        return forEach;
    }

    public void setBefore(SQLExecutorFactory[] before) {
        this.before = before;
    }

    public SQLExecutorFactory[] getBefore() {
        return before;
    }

    public void setAfter(SQLExecutorFactory[] after) {
        this.after = after;
    }

    public SQLExecutorFactory[] getAfter() {
        return after;
    }
}
