package plenix.components.copying.jdbc;

import plenix.components.copying.CopyingException;
import plenix.components.copying.Producer;
import plenix.components.copying.ProducerFactory;
import plenix.components.jdbc.SQLQueryExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

/**
 * JDBCProducerFactory.
 */
public class JDBCProducerFactory extends AbstractJDBCFactory implements ProducerFactory {
    private SQLQueryExecutor queryExecutor;

    /**
     * @see plenix.components.copying.ProducerFactory#newInstance(java.lang.Object)
     */
    public Producer newInstance(Object context) throws CopyingException {
        Connection c = null;
        PreparedStatement s = null;

        try {
            c = getConnection();
            final Connection connection = c;
            final Statement statement = s;

            final ResultSet resultSet =
                    getQueryExecutor().executeQuery(connection, (Map) context);

            return new Producer() {
                private boolean closed = false;

                public Object produce() throws CopyingException {
                    if (this.closed) {
                        throw new CopyingException("Fetch past end of result set");
                    }

                    try {
                        if (resultSet.next()) {
                            return resultSet;
                        } else {
                            this.close();
                            return null;
                        }
                    } catch (Exception e) {
                        this.close();
                        throw new CopyingException("Error fetching next row", e);
                    }
                }

                private void close() {
                    try {
                        this.closed = true;
                        try {
                            resultSet.close();
                        } catch (Exception s) {
                        }
                        try {
                            statement.close();
                        } catch (Exception s) {
                        }
                        try {
                            connection.close();
                        } catch (Exception s) {
                        }
                    } catch (Exception e) {
                    }
                }
            };
        } catch (Exception e) {
            if (s != null) try {
                s.close();
            } catch (Exception x) {
            }
            if (c != null) try {
                c.close();
            } catch (Exception x) {
            }
            throw new CopyingException("Error creating new JDBC object source instance", e);
        }
    }

    public void setQueryExecutor(SQLQueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public SQLQueryExecutor getQueryExecutor() {
        return queryExecutor;
    }
}
