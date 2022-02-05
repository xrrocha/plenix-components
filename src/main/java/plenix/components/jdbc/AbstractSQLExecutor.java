package plenix.components.jdbc;

import plenix.components.logging.AbstractLogEnabled;
import plenix.components.variable.VariableExpander;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * AbstractSQLExecutor.
 */
public abstract class AbstractSQLExecutor extends AbstractLogEnabled {
    private String sql;
    private VariableExpander expander;

    private String expandedSql = null;
    private Set<String> variableReferences = null;

    protected PreparedStatement prepare(Connection connection) throws SQLException {
        if (expandedSql == null) {
            expandedSql = expander.expandVariables(sql, null, "?");
            variableReferences = expander.variableReferences(sql);
        }
        return connection.prepareStatement(expandedSql);
    }

    protected void setParameters(PreparedStatement statement, Map parameters) throws SQLException {
        int i = 0;
        statement.clearParameters();
        for (String variableName : variableReferences) {
            i++;
            ColumnValue value = (ColumnValue) parameters.get(variableName);
            if (value.getValue() == null) {
                statement.setNull(i, value.getType());
            } else {
                statement.setObject(i, value.getValue(), value.getType());
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public void setExpander(VariableExpander variableExpander) {
        this.expander = variableExpander;
    }

    public VariableExpander getExpander() {
        return expander;
    }
}
