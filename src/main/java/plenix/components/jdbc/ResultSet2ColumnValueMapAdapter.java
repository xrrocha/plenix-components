package plenix.components.jdbc;

import plenix.components.adapter.Adapter;
import plenix.components.adapter.AdapterException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

/**
 * ResultSet2ColumnValueMapAdapter.
 */
public class ResultSet2ColumnValueMapAdapter implements Adapter {

    /**
     * @see plenix.components.adapter.Adapter#adapt(java.lang.Object)
     */
    public Object adapt(Object source) throws AdapterException {
        try {
            ResultSet resultSet = (ResultSet) source;
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map map = new HashMap();
            for (int i = 0; i < columnCount; i++) {
                map.put(metaData.getColumnLabel(i + 1),
                        new ColumnValue(metaData.getColumnType(i + 1),
                                resultSet.getObject(i + 1)));
            }
            return map;
        } catch (Exception e) {
            throw new AdapterException("Error adapting result set to map", e);
        }
    }

}
