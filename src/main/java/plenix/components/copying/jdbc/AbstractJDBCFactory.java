package plenix.components.copying.jdbc;

import plenix.components.logging.AbstractLogEnabled;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * AbstractJDBCFactory.
 */
public abstract class AbstractJDBCFactory extends AbstractLogEnabled {
    private DataSource dataSource;

    protected Connection getConnection() throws SQLException {
        Connection connection = getDataSource().getConnection();
        if (connection == null) {
            debug("Null connection returned by datasource");
            throw new IllegalStateException("Null connection returned by datasource");
        }
        return connection;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
